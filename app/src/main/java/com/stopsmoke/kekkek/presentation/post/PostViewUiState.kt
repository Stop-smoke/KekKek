package com.stopsmoke.kekkek.presentation.post

sealed interface PostViewUiState {
    data object InitUiState: PostViewUiState

    data object ErrorExit: PostViewUiState
}