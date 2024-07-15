package com.stopsmoke.kekkek.presentation.error

sealed interface CommonErrorUiState {
    data object InitUiState: CommonErrorUiState
    data object ErrorExit: CommonErrorUiState
    data object ErrorMissing: CommonErrorUiState
}