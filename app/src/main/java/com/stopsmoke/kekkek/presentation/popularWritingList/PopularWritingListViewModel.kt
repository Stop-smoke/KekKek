package com.stopsmoke.kekkek.presentation.popularWritingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularWritingListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PopularWritingListUiState> =
        MutableStateFlow(PopularWritingListUiState.init())
    val uiState: StateFlow<PopularWritingListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val postItems = postRepository.getPopularPostList()
            _uiState.emit(
                PopularWritingListUiState(
                    list = postItems.map { updatePopularWritingListItem(it) }
                )
            )
        }
    }

    private fun updatePopularWritingListItem(post: Post): PopularWritingListItem =
        PopularWritingListItem(
            userInfo = UserInfo(
                name = post.written.name,
                rank = post.written.ranking,
                profileImage = if (post.written.profileImage is ProfileImage.Web) post.written.profileImage.url else "",
                uid = post.written.uid
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
                comment = post.commentUser.size.toLong(),
                id = post.id
            ),
            postImage = "",
            post = post.text,
            postTime = post.modifiedElapsedDateTime,
            postType = post.categories
        )
}