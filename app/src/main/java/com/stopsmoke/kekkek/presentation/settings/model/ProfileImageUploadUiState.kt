package com.stopsmoke.kekkek.presentation.settings.model

sealed interface ProfileImageUploadUiState {

    data object Init : ProfileImageUploadUiState

    data object Success : ProfileImageUploadUiState

    data object Progress : ProfileImageUploadUiState

    data class Error(val t: Throwable?) : ProfileImageUploadUiState
}