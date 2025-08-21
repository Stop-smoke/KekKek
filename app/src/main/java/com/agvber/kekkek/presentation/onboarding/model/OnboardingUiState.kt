package com.agvber.kekkek.presentation.onboarding.model

sealed interface OnboardingUiState {

    data object Success: OnboardingUiState

    data class LoadFail(val t: Throwable?): OnboardingUiState

    data object Loading: OnboardingUiState
}