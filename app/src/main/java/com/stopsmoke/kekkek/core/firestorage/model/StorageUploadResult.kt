package com.stopsmoke.kekkek.core.firestorage.model

sealed interface StorageUploadResult {

    data class Success(val imageUrl: String) : StorageUploadResult
    data class Error(val exception: Throwable? = null) : StorageUploadResult
    data object Progress : StorageUploadResult
}