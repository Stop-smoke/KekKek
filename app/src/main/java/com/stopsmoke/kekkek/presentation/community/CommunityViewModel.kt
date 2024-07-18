package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.emptyActivities
import com.stopsmoke.kekkek.core.domain.model.toPostCategory
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<CommunityUiState> =
        MutableStateFlow(CommunityUiState.init())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    private val _category = MutableStateFlow(PostCategory.ALL)
    val category get() = _category.asStateFlow()

    fun setCategory(categoryString: String) {
        viewModelScope.launch {
            _category.emit(categoryString.toPostCategory())
        }
    }

    val noticeBanner = postRepository.getTopNotice(1)
        .map { post ->
            post.first()
        }.asResult()

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts = category.flatMapLatest { postCategory ->
        postRepository.getPost(postCategory)
            .map { pagingData ->
                pagingData.map { post ->
                    post.toCommunityWritingListItem()
                }
            }
    }
        .cachedIn(viewModelScope)
        .asResult()

    @OptIn(ExperimentalCoroutinesApi::class)
    val topPopularPosts = posts.flatMapLatest {
        postRepository.getPopularPostList().asResult()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val topPopularPostNonPeriod = posts.flatMapLatest {
        postRepository.getPopularPostListNonPeriod().asResult()
    }

    fun bindPopularPosts(popularList: List<Post>, period: Boolean) {
        (uiState.value as? CommunityUiState.CommunityNormalUiState)?.let {
            _uiState.update {
                when (period) {
                    true -> {
                        (it as CommunityUiState.CommunityNormalUiState).copy(
                            popularItem = CommunityPopularItem(
                                postInfo1 = if (popularList.isNotEmpty()) popularList[0].toCommunityWritingListItem() else emptyCommunityWritingListItem(),
                                postInfo2 = if (popularList.size > 1) popularList[1].toCommunityWritingListItem() else emptyCommunityWritingListItem()
                            ), popularPeriod = if(popularList.size < 2) false else true
                        )
                    }

                    false -> (it as CommunityUiState.CommunityNormalUiState).copy(
                        popularItemNonPeriod = CommunityPopularItem(
                            postInfo1 = if (popularList.isNotEmpty()) popularList[0].toCommunityWritingListItem() else emptyCommunityWritingListItem(),
                            postInfo2 = if (popularList.size > 1) popularList[1].toCommunityWritingListItem() else emptyCommunityWritingListItem()
                        )
                    )
                }
            }
        }
    }
}
