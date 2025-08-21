package com.agvber.kekkek.presentation.onboarding.model

sealed interface AuthenticationEvent {

    data object AlreadyUser : AuthenticationEvent

    data object NewMember : AuthenticationEvent

    data object Init : AuthenticationEvent

    data class Error(val t: Throwable?) : AuthenticationEvent

    data object Guest : AuthenticationEvent
}