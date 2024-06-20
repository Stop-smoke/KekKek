package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<CommunityUiState> =
        MutableStateFlow(CommunityUiState.init())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    private val _isPostChanged = MutableStateFlow(false)
    val isPostChanged: StateFlow<Boolean> get() = _isPostChanged.asStateFlow()


    fun setPostChanged(isChanged: Boolean) {
        viewModelScope.launch {
            _isPostChanged.emit(isChanged)
        }
    }

    private val _category = MutableStateFlow(PostCategory.ALL)
    val category get() = _category.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<CommunityWritingItem>> = category.flatMapLatest { postCategory ->
        postRepository.getPost(postCategory)
            .map { pagingData ->
                pagingData.map { post ->
                    post.toCommunityWritingListItem()
                }
            }
    }
        .cachedIn(viewModelScope)
        .catch {
            it.printStackTrace()
        }


    val topPopularPosts = posts.flatMapLatest {
        postRepository.getTopPopularItems()
    }


    private fun emptyPostInfo() = PostInfo(
        title = "",
        postType = "",
        view = 0,
        like = 0,
        comment = 0,
        id = ""
    )

    private fun updatePostInfo(post: Post): PostInfo = PostInfo(
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
        comment = post.commentCount,
        id = post.id
    )

    private fun updateWritingItem(post: Post): CommunityWritingItem =
        CommunityWritingItem(
            userInfo = UserInfo(
                uid = post.written.uid,
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
                comment = post.commentCount,
                id = post.id
            ),
            postImage = "",
            post = post.text,
            postTime = post.modifiedElapsedDateTime,
            postType = post.categories
        )

    fun setCategory(categoryString: String) {
        when (categoryString) {
            "커뮤니티 홈" -> {
                updateCategory(PostCategory.ALL)
            }

            "공지사항" -> {
                updateCategory(PostCategory.NOTICE)
            }

            "금연 지원 프로그램 공지" -> {
                updateCategory(PostCategory.QUIT_SMOKING_SUPPORT)
            }

            "인기글" -> {
                updateCategory(PostCategory.POPULAR)
            }

            "금연 보조제 후기" -> {
                updateCategory(PostCategory.QUIT_SMOKING_AIDS_REVIEWS)
            }

            "금연 성공 후기" -> {
                updateCategory(PostCategory.SUCCESS_STORIES)
            }

            "자유게시판" -> {
                updateCategory(PostCategory.GENERAL_DISCUSSION)
            }

            "금연 실패 후기" -> {
                updateCategory(PostCategory.FAILURE_STORIES)
            }

            "금연 다짐" -> {
                updateCategory(PostCategory.RESOLUTIONS)
            }

            else -> {
                updateCategory(PostCategory.UNKNOWN)
            }
        }
    }

    private fun updateCategory(postCategory: PostCategory) {
        _category.value = postCategory
    }

    private val _noticeBanner = MutableStateFlow(Post.emptyPost())
    val noticeBanner: StateFlow<Post> get() = _noticeBanner.asStateFlow()

    init {
        viewModelScope.launch {
            val noticeBannerPost = postRepository.getTopNotice()
            _noticeBanner.emit(noticeBannerPost)
        }
    }

    fun bindPopularPosts(popularList: List<Post>) {
        _uiState.value = (
                CommunityUiState.CommunityNormalUiState(
                    popularItem = CommunityPopularItem(
                        postInfo1 = if (popularList.isNotEmpty()) popularList[0].toCommunityWritingListItem() else emptyCommunityWritingListItem(),
                        postInfo2 = if (popularList.size > 1) popularList[1].toCommunityWritingListItem() else emptyCommunityWritingListItem()
                    )
                )
                )
    }

}
