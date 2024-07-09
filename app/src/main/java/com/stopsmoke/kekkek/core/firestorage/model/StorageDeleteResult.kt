package com.stopsmoke.kekkek.core.firestorage.model

sealed interface StorageDeleteResult {

    data object Success : StorageDeleteResult

    data class Error(val exception: Throwable? = null) : StorageDeleteResult
}