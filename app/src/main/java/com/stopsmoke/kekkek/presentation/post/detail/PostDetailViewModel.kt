package com.stopsmoke.kekkek.presentation.post.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.CommentFilter
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.repository.CommentRepository
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.core.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.domain.usecase.AddCommentUseCase
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
import kotlinx.coroutines.flow.update
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

    private val _postDetailUiState: MutableStateFlow<PostDetailUiState> =
        MutableStateFlow(PostDetailUiState.init())
    val postDetailUiState = _postDetailUiState.asStateFlow()


    private val _postId: MutableStateFlow<String?> = MutableStateFlow(null)
    val postId = _postId.asStateFlow()

    fun updatePostId(id: String) {
        viewModelScope.launch {
            _postId.emit(id)
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            try {
                postRepository.deletePost(postId.value!!)
            } catch (e: Exception) {
                e.printStackTrace()
                _postDetailUiState.emit(PostDetailUiState.ErrorExit)
            }
        }
    }

    val user: StateFlow<User?> = userRepository.getUserData()
        .catch {
            it.printStackTrace()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val post = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }
        postRepository.getPostItem(it)
    }
        .catch {
            it.printStackTrace()
            _postDetailUiState.value = PostDetailUiState.ErrorExit
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _previewCommentItem: MutableStateFlow<List<Comment>> = MutableStateFlow(emptyList())
    val previewCommentItem = _previewCommentItem.asStateFlow()

    private val commentTransferLike: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    private val replyTransferLike: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    private val commentDeleteSet: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())
    private val replyDeleteIdSet: MutableStateFlow<Set<String>> = MutableStateFlow(emptySet())

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }
        commentRepository.getCommentItems(CommentFilter.Post(it))
    }
        .map { pagingData ->
            pagingData.map {
                PostDetailCommentRecyclerViewUiState.CommentType(it)
            }
                .insertSeparators { before, after ->
                    if (before == null) {
                        return@insertSeparators PostDetailCommentRecyclerViewUiState.Header
                    }

                    if (before.item.earliestReply.isNotEmpty()) {
                        return@insertSeparators PostDetailCommentRecyclerViewUiState.ReplyType(
                            before.item
                        )
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
                                uiState.item.likeUser.toggleElement(user.value!!.uid)
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
                                    likeUser = reply.likeUser.toggleElement(user.value!!.uid)
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
        .combine(replyDeleteIdSet) { pagingData, replyDeleteIdSet ->
            pagingData.map { uiState ->
                when (uiState) {
                    is PostDetailCommentRecyclerViewUiState.CommentType -> uiState
                    is PostDetailCommentRecyclerViewUiState.Deleted -> uiState
                    is PostDetailCommentRecyclerViewUiState.Header -> uiState
                    is PostDetailCommentRecyclerViewUiState.ReplyType -> {
                        val earliestReply = uiState.item.earliestReply.mapNotNull { reply ->
                            if (replyDeleteIdSet.contains(reply.id)) {
                                return@mapNotNull null
                            }
                            reply
                        }
                        return@map uiState.copy(uiState.item.copy(earliestReply = earliestReply))
                    }
                }
            }
        }
        .catch {
            it.printStackTrace()
            _postDetailUiState.emit(PostDetailUiState.ErrorExit)
        }

    fun addComment(text: String) = viewModelScope.launch {
        try {
            if (post.value == null) return@launch

            val newCommentId = addCommentUseCase(
                postId = post.value!!.id,
                postTitle = post.value!!.title,
                postType = post.value!!.category,
                text = text
            )
            val newComment = commentRepository.getComment(postId.value!!, newCommentId)

            _previewCommentItem.update {
                it.toMutableList().apply { add(newComment.first()) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _postDetailUiState.emit(PostDetailUiState.ErrorExit)
        }
    }

    fun deleteComment(commentId: String) = viewModelScope.launch {
        try {
            val previewComment = previewCommentItem.value.find { it.id == commentId }

            if (previewComment == null) {
                commentDeleteSet.emit(commentDeleteSet.value.toggleElement(commentId))
            } else {
                _previewCommentItem.update {
                    it.toMutableList().apply { remove(previewComment) }
                }
            }
            commentRepository.deleteCommentItem(post.value!!.id, commentId)
        } catch (e: Exception) {
            e.printStackTrace()
            _postDetailUiState.emit(PostDetailUiState.ErrorExit)
        }
    }

    fun toggleLikeToPost() = try {
        viewModelScope.launch {
            val user = user.value ?: return@launch
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
        _postDetailUiState.value = PostDetailUiState.ErrorExit
    }

    fun toggleBookmark() = try {
        viewModelScope.launch {
            val user = user.value ?: return@launch
            val postId = postId.value ?: return@launch

            if (post.value?.bookmarkUser?.contains(user.uid) == true) {
                postRepository.deleteBookmark(postId)
                return@launch
            }
            postRepository.addBookmark(postId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        _postDetailUiState.value = PostDetailUiState.ErrorExit
    }

    fun toggleCommentLike(comment: Comment) = viewModelScope.launch {
        try {
            val previewComment = previewCommentItem.value.find { it.id == comment.id }

            if (previewComment == null) {
                commentTransferLike.emit(commentTransferLike.value.toggleElement(comment.id))
            } else {
                _previewCommentItem.update { comments ->
                    comments.toMutableList().apply {
                        val newComment = previewComment.copy(
                            isLiked = !previewComment.isLiked,
                            likeUser = previewComment.likeUser.toggleElement(user.value!!.uid)
                        )
                        set(indexOf(previewComment), newComment)
                    }
                }
            }

            if (comment.isLiked) {
                commentRepository.removeCommentLike(post.value!!.id, comment.id)
            } else {
                commentRepository.addCommentLike(post.value!!.id, comment.id)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _postDetailUiState.value = PostDetailUiState.ErrorExit
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
            _postDetailUiState.value = PostDetailUiState.ErrorExit
        }
    }

    fun deleteReply(commentId: String, replyId: String) = viewModelScope.launch {
        try {
            replyDeleteIdSet.update { it.toMutableSet().apply { add(replyId) } }
            replyRepository.deleteReply(postId.value!!, commentId, replyId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}