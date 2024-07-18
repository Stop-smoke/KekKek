package com.stopsmoke.kekkek.presentation.settings.profile.model

sealed interface ExitAppUiState {

    data object Logout : ExitAppUiState

    data object Withdraw : ExitAppUiState

    data class Failure(val t: Throwable?) : ExitAppUiState
}