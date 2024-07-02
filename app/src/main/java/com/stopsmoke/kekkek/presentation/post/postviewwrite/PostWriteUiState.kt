package com.stopsmoke.kekkek.presentation.post.postviewwrite

sealed interface PostWriteUiState {
    data object InitUiState: PostWriteUiState

    data object LadingUiState: PostWriteUiState

    data object Success: PostWriteUiState

    data object ErrorExit: PostWriteUiState
}