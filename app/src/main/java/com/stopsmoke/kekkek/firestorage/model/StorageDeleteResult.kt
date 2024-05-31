package com.stopsmoke.kekkek.firestorage.model

sealed interface StorageDeleteResult {

    data object Success : StorageDeleteResult

    data class Error(val exception: Throwable? = null) : StorageDeleteResult
}