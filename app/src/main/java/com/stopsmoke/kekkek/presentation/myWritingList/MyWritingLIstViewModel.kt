package com.stopsmoke.kekkek.presentation.myWritingList

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyWritingLIstViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<MyWritingListUiState> =
        MutableStateFlow(MyWritingListUiState.init())
    val uiState: StateFlow<MyWritingListUiState> = _uiState.asStateFlow()
}