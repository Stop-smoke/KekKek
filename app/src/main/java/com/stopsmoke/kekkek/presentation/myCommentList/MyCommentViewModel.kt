package com.stopsmoke.kekkek.presentation.myCommentList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyCommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    val _userState: MutableStateFlow<User> = MutableStateFlow(User.Guest)
    val userState = _userState.asStateFlow()

    private var communityWritingItem: CommunityWritingItem? = null


    val myCommentPosts = userState.flatMapLatest { userData ->
        commentRepository.getCommentItems(if (userData is User.Registered) userData.commentMy else emptyList())
            .let {
                when (it) {
                    is Result.Error -> {
                        it.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData ->
                        pagingData.map { comment ->
                            updateMyWritingListItem(comment)
                        }
                    }
                }
            }.cachedIn(viewModelScope)
    }

    fun setCommunityWritingItem(postId: String) {
        viewModelScope.launch {
            val post = postRepository.getPostForPostId(postId)
            communityWritingItem = post.toCommunityWritingListItem()
        }
    }

    fun getCommunityWritingItem() = communityWritingItem

    fun updateUserState() = viewModelScope.launch {
        try {
            val userDataResultFlow = userRepository.getUserData("테스트_계정")
            when (userDataResultFlow) {
                is Result.Success -> {
                    userDataResultFlow.data.collect {
                        _userState.value = it
                    }
                }

                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateMyWritingListItem(comment: Comment): MyCommentItem =
        MyCommentItem(
            postData = comment.postData,
            commentId = comment.id,
            content = comment.text,
            time = comment.dateTime.created
        )
}