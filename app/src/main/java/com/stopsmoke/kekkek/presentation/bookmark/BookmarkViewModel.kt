package com.stopsmoke.kekkek.presentation.bookmark

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.presentation.community.CommunityUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BookmarkViewModel:ViewModel() {
    private val _uiState: MutableStateFlow<BookmarkUiState> = MutableStateFlow(BookmarkUiState.init())
    val uiState: StateFlow<BookmarkUiState> = _uiState.asStateFlow()
}