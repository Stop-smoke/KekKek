package com.stopsmoke.kekkek.firestorage.model

interface StorageUploadResult {

    data class Success(val imageUrl: String) : StorageUploadResult
    data class Error(val exception: Throwable? = null) : StorageUploadResult
    data object Progress : StorageUploadResult
}