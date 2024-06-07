package com.stopsmoke.kekkek.presentation.noticeWritingList

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoticeListViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<NoticeListUiState> =
        MutableStateFlow(NoticeListUiState.init())
    val uiState: StateFlow<NoticeListUiState> = _uiState.asStateFlow()
}