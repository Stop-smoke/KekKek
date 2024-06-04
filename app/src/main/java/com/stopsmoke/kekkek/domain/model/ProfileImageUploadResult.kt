package com.stopsmoke.kekkek.domain.model

sealed interface ProfileImageUploadResult {

    data object Success : ProfileImageUploadResult

    data class Error(val exception: Throwable? = null) : ProfileImageUploadResult

    data object Progress : ProfileImageUploadResult
}