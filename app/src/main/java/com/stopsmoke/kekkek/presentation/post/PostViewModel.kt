package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    userRepository: UserRepository,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }

        commentRepository.getCommentItems(CommentFilter.Post(it))
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

    fun addComment(text: String) {
        viewModelScope.launch {
            if (post.value == null) return@launch
            val user = user.firstOrNull() as? User.Registered ?: return@launch

            val comment = Comment(
                id = "",
                text = text,
                dateTime = LocalDateTime.now().let { DateTime(it, it) },
                likeUser = emptyList(),
                unlikeUser = emptyList(),
                reply = emptyList(),
                written = Written(
                    uid = user.uid,
                    name = user.name,
                    profileImage = user.profileImage,
                    ranking = user.ranking
                )
            )
            commentRepository.addCommentItem(post.value!!.id, comment)
        }
    }

    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            postId.value?.let { commentRepository.deleteCommentItem(it, commentId) }
        }
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

    fun toggleLikeToPost() {
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
    }

    fun toggleBookmark() {
        viewModelScope.launch {
            val user = user.first() as? User.Registered ?: return@launch
            val postId = postId.value ?: return@launch

            if (post.value?.bookmarkUser?.contains(user.uid) == true) {
                postRepository.deleteBookmark(postId)
                return@launch
            }
            postRepository.addBookmark(postId)
        }
    }
}