package com.stopsmoke.kekkek.presentation.settings.profile.model

sealed interface ExitAppUiState {

    data object Loading : ExitAppUiState

    data object Success : ExitAppUiState

    data class Failure(val t: Throwable?) : ExitAppUiState
}