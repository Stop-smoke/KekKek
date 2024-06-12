package com.stopsmoke.kekkek.presentation.myWritingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWritingLIstViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val _userState: MutableStateFlow<User> = MutableStateFlow(User.Guest)
    val userState = _userState.asStateFlow()

    val myWritingPosts = userState.flatMapLatest { userData ->
        postRepository.getPostForWrittenUid(if (userData is User.Registered) userData.uid else "")
            .let {
                when (it) {
                    is Result.Error -> {
                        it.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData ->
                        pagingData.map { post ->
                            updateMyWritingListItem(post)
                        }
                    }
                }
            }.cachedIn(viewModelScope)
    }

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

    private fun updateMyWritingListItem(post: Post): MyWritingListItem =
        MyWritingListItem(
            userInfo = UserInfo(
                name = post.written.name,
                rank = post.written.ranking,
                profileImage = if (post.written.profileImage is ProfileImage.Web) post.written.profileImage.url else ""
            ),
            postInfo = PostInfo(
                title = post.title,
                postType = when (post.categories) {
                    PostCategory.NOTICE -> "공지사항"
                    PostCategory.QUIT_SMOKING_SUPPORT -> " 금연 지원 프로그램 공지"
                    PostCategory.POPULAR -> "인기글"
                    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "금연 보조제 후기"
                    PostCategory.SUCCESS_STORIES -> "금연 성공 후기"
                    PostCategory.GENERAL_DISCUSSION -> "자유게시판"
                    PostCategory.FAILURE_STORIES -> "금연 실패 후기"
                    PostCategory.RESOLUTIONS -> "금연 다짐"
                    PostCategory.UNKNOWN -> ""
                    PostCategory.ALL -> ""
                },
                view = post.views,
                like = post.likeUser.size.toLong(),
                comment = post.commentUser.size.toLong()
            ),
            postImage = "",
            post = post.text,
            postTime = post.modifiedElapsedDateTime,
            postType = post.categories
        )
}