package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.stopsmoke.kekkek.addOrRemove
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
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

        postRepository.addViews(it)
        postRepository.getPostItem(it)

    }
        .flatMapLatest {
            flowOf(it.first())
        }
        .catch {
            it.printStackTrace()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val replyTransferLike: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment: Flow<PagingData<CommentUiState>> = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }
        commentRepository.getCommentItems(CommentFilter.Post(it))
    }
        .map { pagingData ->
            pagingData
                .map {
                    CommentUiState.CommentType(it)
                }
                .insertSeparators { before, after ->
                    if (before == null) {
                        return@insertSeparators CommentUiState.Header
                    }

                    if (before.item.earliestReply.isNotEmpty()) {
                        return@insertSeparators CommentUiState.ReplyType(before.item)
                    }

                    null
                }
        }
        .cachedIn(viewModelScope)
        .combine(replyTransferLike) { pagingData, replyLikeList ->
            pagingData.map { uiState ->
                when (uiState) {
                    is CommentUiState.CommentType -> uiState
                    is CommentUiState.Header -> uiState
                    is CommentUiState.ReplyType -> {
                        val replyList = uiState.item.earliestReply.map { reply ->
                            if (replyLikeList.contains(reply.id)) {
                                reply.copy(
                                    isLiked = !reply.isLiked,
                                    likeUser = reply.likeUser.addOrRemove((user.value as User.Registered).uid)
                                )
                            } else {
                                reply
                            }
                        }
                        uiState.copy(uiState.item.copy(earliestReply = replyList))
                    }
                }
            }
        }
        .catch {
            it.printStackTrace()
        }

    fun addComment(text: String, postTitle: String) {
        try {
            viewModelScope.launch {
                if (post.value == null) return@launch
                addCommentUseCase(
                    postId = post.value!!.id,
                    postTitle = postTitle,
                    postType = post.value!!.category,
                    text = text
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteComment(commentId: String) = try {
        viewModelScope.launch {
            postId.value?.let { commentRepository.deleteCommentItem(it, commentId) }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val commentCount = postId.flatMapLatest { id ->
        if (id.isNullOrBlank()) {
            return@flatMapLatest emptyFlow()
        }

        commentRepository.getCommentCount(id)
    }
        .catch {
            it.printStackTrace()
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

    fun commentLikeClick(comment: Comment) = viewModelScope.launch {
        try {
            commentRepository.setCommentItem(comment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toggleReplyLike(reply: Reply) = viewModelScope.launch {
        try {
            val replyList = replyTransferLike.value.addOrRemove(reply.id)
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

sealed interface CommentUiState {

    data object Header : CommentUiState

    data class CommentType(val item: Comment) : CommentUiState

    data class ReplyType(val item: Comment) : CommentUiState

}