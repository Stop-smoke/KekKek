package com.stopsmoke.kekkek.presentation.onboarding.model

sealed interface OnboardingUiState {

    data object Success: OnboardingUiState

    data object LoadFail: OnboardingUiState

    data object Loading: OnboardingUiState
}