package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<CommunityUiState> = MutableStateFlow(CommunityUiState.init())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    private var posts: Flow<PagingData<CommunityWritingItem>> = postRepository.getPost()
        .let {
            when (it) {
                is Result.Error -> emptyFlow()

                is Result.Loading -> emptyFlow()
                is Result.Success -> it.data.map { pagingData->
                    pagingData.map {
                        updateWritingItem(it)
                    }
                }
            }
        }
        .cachedIn(viewModelScope)

    fun getPosts() = posts
    fun reLoading() =  viewModelScope.launch {
        posts = postRepository.getPost()
            .let {
                when (it) {
                    is Result.Error -> emptyFlow()
                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData->
                        pagingData.map {
                            updateWritingItem(it)
                        }
                    }
                }
            }
            .cachedIn(viewModelScope)
    }
    private fun updateWritingItem(post: Post): CommunityWritingItem =
        CommunityWritingItem(
            userInfo = UserInfo(
                name = post.written.name,
                rank = post.written.ranking,
                profileImage = if(post.written.profileImage is ProfileImage.Web) post.written.profileImage.url else ""
            ),
            postInfo = PostInfo(
                title = post.title,
                postType = when(post.categories){
                    PostCategory.NOTICE -> "공지사항"
                    PostCategory.QUIT_SMOKING_SUPPORT -> "금연 보조제 후기"
                    PostCategory.POPULAR -> "인기 게시글"
                    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> ""
                    PostCategory.SUCCESS_STORIES -> ""
                    PostCategory.GENERAL_DISCUSSION -> ""
                    PostCategory.FAILURE_STORIES -> ""
                    PostCategory.RESOLUTIONS -> ""
                    PostCategory.UNKNOWN -> ""
                },
                view = post.views,
                like = post.likeUser.size.toLong(),
                comment = post.commentUser.size.toLong()
            ),
            postImage = "",
            post = post.text,
            postTime = post.modifiedElapsedDateTime
        )
}
