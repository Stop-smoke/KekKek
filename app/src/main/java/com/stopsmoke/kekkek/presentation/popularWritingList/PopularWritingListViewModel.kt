package com.stopsmoke.kekkek.presentation.popularWritingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularWritingListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PopularWritingListUiState> =
        MutableStateFlow(PopularWritingListUiState.init())
    val uiState: StateFlow<PopularWritingListUiState> = _uiState.asStateFlow()

    val post = uiState.flatMapLatest { popularWritingListUiState ->
        flowOf( postRepository.getPopularPostList().map { it.toCommunityWritingListItem() })
    }


    fun reload(){
        _uiState.update { prev ->
            prev.copy(isLoading = true)
        }
    }
}