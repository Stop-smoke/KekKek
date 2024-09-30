package com.agvber.kekkek.presentation.post.edit

sealed interface PostEditUiState {
    data object InitUiState: PostEditUiState

    data object LadingUiState: PostEditUiState

    data object Success: PostEditUiState

    data object ErrorExit: PostEditUiState
}