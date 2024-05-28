package com.stopsmoke.kekkek.myFragment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MyViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<MyUiState> = MutableStateFlow(MyUiState.init())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()
}
