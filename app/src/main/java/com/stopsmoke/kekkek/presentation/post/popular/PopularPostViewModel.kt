package com.stopsmoke.kekkek.presentation.post.popular

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PopularPostViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PopularPostUiState> =
        MutableStateFlow(PopularPostUiState.init())
    val uiState: StateFlow<PopularPostUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val post = uiState.flatMapLatest {
        postRepository.getPopularPostList().asResult()
    }

}