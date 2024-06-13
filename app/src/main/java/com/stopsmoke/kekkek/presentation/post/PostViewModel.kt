package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _postId: MutableStateFlow<String?> = MutableStateFlow(null)
    val postId = _postId.asStateFlow()

    private val _bookmarkPosts = MutableLiveData<List<CommunityWritingItem>>()
    val bookmarkPosts : LiveData<List<CommunityWritingItem>> get() = _bookmarkPosts

    private val currentBookmarkPost = arrayListOf<CommunityWritingItem>()

    fun addBookmarkPost(post: CommunityWritingItem) {
        currentBookmarkPost.add(post)
    }

    fun deleteBookmarkPost(post: CommunityWritingItem) {
        currentBookmarkPost.remove(post)
    }

    fun updateMyBookmark() {
        _bookmarkPosts.postValue(currentBookmarkPost)
    }

    fun updatePostId(id: String) {
        viewModelScope.launch {
            _postId.emit(id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment = postId.flatMapLatest {
        if (it == null) {
            return@flatMapLatest emptyFlow()
        }

        commentRepository.getCommentItems(CommentFilter.Post(it))
            .let { result ->
                when(result) {
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

    val user = userRepository.getUserData()

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

}