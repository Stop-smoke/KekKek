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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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

    val user = userRepository.getUserData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val post: StateFlow<List<Post>> = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }

        postRepository.addViews(it)
        postRepository.getPostItem(it)
            .let { result ->
                when (result) {
                    is Result.Error -> emptyFlow()
                    is Result.Loading -> emptyFlow()
                    is Result.Success -> result.data
                }
            }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }

        commentRepository.getCommentItems(CommentFilter.Post(it))
            .let { result ->
                when (result) {
                    is Result.Error -> {
                        result.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> result.data
                }
            }
            .cachedIn(viewModelScope)
    }

    fun addComment(commentPostData: CommentPostData, text: String) {
        viewModelScope.launch {
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
                ),
                postData = commentPostData
            )
            commentRepository.addCommentItem(comment)
        }
    }

    fun deleteComment(commentId: String) {
        viewModelScope.launch {
            commentRepository.deleteCommentItem(commentId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val commentCount = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }

        commentRepository.getCommentCount(it)
            .let { result ->
                when (result) {
                    is Result.Error -> emptyFlow()
                    is Result.Loading -> emptyFlow()
                    is Result.Success -> result.data
                }
            }
    }

    fun toggleLikeToPost() {
        viewModelScope.launch {
            val user = user.first() as? User.Registered ?: return@launch
            val postId = postId.value ?: return@launch
            val currentPost = post.value.firstOrNull() ?: return@launch

            if (!currentPost.likeUser.contains(user.uid)) {
                postRepository.addLikeToPost(postId)
                return@launch
            }

            postRepository.deleteLikeToPost(postId)
        }
    }

}