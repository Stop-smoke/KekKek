package com.stopsmoke.kekkek.presentation.popularWritingList

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PopularWritingListViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<PopularWritingListUiState> =
        MutableStateFlow(PopularWritingListUiState.init())
    val uiState: StateFlow<PopularWritingListUiState> = _uiState.asStateFlow()
}