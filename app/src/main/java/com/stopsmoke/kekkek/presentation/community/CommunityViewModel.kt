package com.stopsmoke.kekkek.presentation.community

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.presentation.my.MyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CommunityViewModel: ViewModel() {
    private val _uiState: MutableStateFlow<CommunityUiState> = MutableStateFlow(CommunityUiState.init())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()
}