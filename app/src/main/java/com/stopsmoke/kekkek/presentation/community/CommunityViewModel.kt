package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.mapper.toPostCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val postRepository: PostRepository,
) : ViewModel() {
    private val _communityUiState: MutableStateFlow<CommunityUiState> =
        MutableStateFlow(CommunityUiState.init())
    val communityUiState: StateFlow<CommunityUiState> = _communityUiState.asStateFlow()

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
        (communityUiState.value as? CommunityUiState.CommunityNormalUiState)?.let {
            _communityUiState.update {
                when (period) {
                    true -> {
                        (it as CommunityUiState.CommunityNormalUiState).copy(
                            popularItem = popularList.map { it.toCommunityWritingListItem() },
                            popularPeriod = if (popularList.size < 2) false else true
                        )
                    }

                    false -> (it as CommunityUiState.CommunityNormalUiState).copy(
                        popularItemNonPeriod = popularList.map { it.toCommunityWritingListItem() },
                    )
                }
            }
        }
    }
}
