package com.agvber.kekkek.presentation.onboarding.model

sealed interface AuthenticationUiState {

    data object AlreadyUser : AuthenticationUiState

    data object NewMember : AuthenticationUiState

    data object Init : AuthenticationUiState

    data class Error(val t: Throwable?) : AuthenticationUiState

    data object Guest : AuthenticationUiState
}