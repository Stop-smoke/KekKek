package com.stopsmoke.kekkek.presentation.smokingaddictiontest

sealed interface SmokingAddictionTestUiState {
    data object NormalUiState: SmokingAddictionTestUiState
    data object ErrorExit: SmokingAddictionTestUiState
}