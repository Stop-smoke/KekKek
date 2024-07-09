package com.stopsmoke.kekkek.presentation.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.domain.usecase.AddCommentUseCase
import com.stopsmoke.kekkek.presentation.post.detail.model.PostDetailCommentRecyclerViewUiState
import com.stopsmoke.kekkek.presentation.toggleElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    userRepository: UserRepository,
    private val addCommentUseCase: AddCommentUseCase,
    private val replyRepository: ReplyRepository,
) : ViewModel() {

    private val _postId: MutableStateFlow<String?> = MutableStateFlow(null)
    val postId = _postId.asStateFlow()

    fun updatePostId(id: String) {
        viewModelScope.launch {
            _postId.emit(id)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
        }
    }

    val user: StateFlow<User?> = userRepository.getUserData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val post: StateFlow<Post?> = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }
        postRepository.getPostItem(it)

    }
        .catch {
            it.printStackTrace()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _previewCommentItem: MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
    val previewCommentItem get() = _previewCommentItem.asStateFlow()

    fun updatePreviewCommentItem(comment: Comment) {
        viewModelScope.launch {
            val newComment = previewCommentItem.value.toMutableList()
            newComment.add(comment)
            _previewCommentItem.emit(newComment)
        }
    }

    private val commentTransferLike: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    private val replyTransferLike: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    private val commentDeleteSet: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }
        commentRepository.getCommentItems(CommentFilter.Post(it))
    }
        .map { pagingData ->
            pagingData
                .map {
                    PostDetailCommentRecyclerViewUiState.CommentType(it)
                }
                .insertSeparators { before, after ->
                    if (before == null) {
                        return@insertSeparators PostDetailCommentRecyclerViewUiState.Header
                    }

                    if (before.item.earliestReply.isNotEmpty()) {
                        return@insertSeparators PostDetailCommentRecyclerViewUiState.ReplyType(before.item)
                    }

                    null
                }
        }
        .cachedIn(viewModelScope)
        .combine(commentDeleteSet) { pagingData, commentDeleteItemId ->
            pagingData.map { uiState ->
                if (
                    uiState is PostDetailCommentRecyclerViewUiState.CommentType &&
                    commentDeleteItemId.contains(uiState.item.id)
                    ) {
                    return@map PostDetailCommentRecyclerViewUiState.Deleted
                }
                uiState
            }
        }
        .combine(commentTransferLike) { pagingData, commentLikeList ->
            pagingData.map { uiState ->
                when (uiState) {
                    is PostDetailCommentRecyclerViewUiState.CommentType -> {
                        if (commentLikeList.contains(uiState.item.id)) {
                            val likeUser =
                                uiState.item.likeUser.toggleElement((user.value as User.Registered).uid)
                            return@map uiState.copy(
                                uiState.item.copy(
                                    likeUser = likeUser,
                                    isLiked = !uiState.item.isLiked
                                )
                            )
                        }
                        uiState
                    }

                    is PostDetailCommentRecyclerViewUiState.Header -> uiState
                    is PostDetailCommentRecyclerViewUiState.ReplyType -> uiState
                    is PostDetailCommentRecyclerViewUiState.Deleted -> uiState
                }
            }
        }
        .combine(replyTransferLike) { pagingData, replyLikeList ->
            pagingData.map { uiState ->
                when (uiState) {
                    is PostDetailCommentRecyclerViewUiState.CommentType -> uiState
                    is PostDetailCommentRecyclerViewUiState.Header -> uiState
                    is PostDetailCommentRecyclerViewUiState.ReplyType -> {
                        val replyList = uiState.item.earliestReply.map { reply ->
                            if (replyLikeList.contains(reply.id)) {
                                reply.copy(
                                    isLiked = !reply.isLiked,
                                    likeUser = reply.likeUser.toggleElement((user.value as User.Registered).uid)
                                )
                            } else {
                                reply
                            }
                        }
                        uiState.copy(uiState.item.copy(earliestReply = replyList))
                    }
                    is PostDetailCommentRecyclerViewUiState.Deleted -> uiState
                }
            }
        }
        .catch {
            it.printStackTrace()
        }

    fun addComment(text: String) {
        try {
            viewModelScope.launch {
                if (post.value == null) return@launch
                val newCommentDocId = addCommentUseCase(
                    postId = post.value!!.id,
                    postTitle = post.value!!.title,
                    postType = post.value!!.category,
                    text = text
                )

                updatePreviewCommentItem(
                    commentRepository.getComment(postId.value!!, newCommentDocId).first()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteComment(commentId: String) = try {
        viewModelScope.launch {
            commentDeleteSet.emit(commentDeleteSet.value.toggleElement(commentId))
            commentRepository.deleteCommentItem(post.value!!.id, commentId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun toggleLikeToPost() = try {
        viewModelScope.launch {
            val user = user.first() as? User.Registered ?: return@launch
            val postId = postId.value ?: return@launch
            val currentPost = post.value ?: return@launch

            if (!currentPost.likeUser.contains(user.uid)) {
                postRepository.addLikeToPost(postId)
                return@launch
            }

            postRepository.deleteLikeToPost(postId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun toggleBookmark() = try {
        viewModelScope.launch {
            val user = user.first() as? User.Registered ?: return@launch
            val postId = postId.value ?: return@launch

            if (post.value?.bookmarkUser?.contains(user.uid) == true) {
                postRepository.deleteBookmark(postId)
                return@launch
            }
            postRepository.addBookmark(postId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun toggleCommentLike(comment: Comment) = viewModelScope.launch {
        try {
            if (comment.isLiked) {
                commentRepository.removeCommentLike(post.value!!.id, comment.id)
            } else {
                commentRepository.addCommentLike(post.value!!.id, comment.id)
            }
            commentTransferLike.emit(commentTransferLike.value.toggleElement(comment.id))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleReplyLike(reply: Reply) = viewModelScope.launch {
        try {
            val replyList = replyTransferLike.value.toggleElement(reply.id)
            replyTransferLike.emit(replyList)

            if (reply.isLiked) {
                replyRepository.removeReplyLike(
                    postId = reply.commentParent.postId,
                    commentId = reply.replyParent,
                    replyId = reply.id
                )
                return@launch
            }

            replyRepository.appendReplyLike(
                postId = reply.commentParent.postId,
                commentId = reply.replyParent,
                replyId = reply.id
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}