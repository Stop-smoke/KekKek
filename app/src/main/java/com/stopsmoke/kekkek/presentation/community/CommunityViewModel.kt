package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.toPostCategory
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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


    private val _category = MutableStateFlow(PostCategory.ALL)
    val category get() = _category.asStateFlow()

    private val _noticeBanner = MutableStateFlow(Post.emptyPost())
    val noticeBanner: StateFlow<Post> get() = _noticeBanner.asStateFlow()


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

    fun setCategory(categoryString: String) {
        updateCategory(categoryString.toPostCategory())
    }

    private fun updateCategory(postCategory: PostCategory) {
        _category.value = postCategory
    }

    init {
        viewModelScope.launch {
            val noticeBannerPost = postRepository.getTopNotice()
            _noticeBanner.emit(noticeBannerPost)
        }
    }

    fun setPostChanged(isChanged: Boolean) {
        viewModelScope.launch {
            _isPostChanged.emit(isChanged)
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
