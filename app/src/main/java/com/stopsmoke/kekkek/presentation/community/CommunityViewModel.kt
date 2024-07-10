package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.toPostCategory
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    val noticeBanner: Flow<Post> = postRepository.getTopNotice(1)
        .map { post ->
            post.first()
        }
        .catch {
            _uiState.emit(CommunityUiState.ErrorExit)
        }

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
            _uiState.emit(CommunityUiState.ErrorExit)
            it.printStackTrace()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val topPopularPosts = posts.flatMapLatest {
        try {
            postRepository.getTopPopularItems()
        } catch (e: Exception) {
            _uiState.emit(CommunityUiState.ErrorExit)
            emptyFlow()
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
