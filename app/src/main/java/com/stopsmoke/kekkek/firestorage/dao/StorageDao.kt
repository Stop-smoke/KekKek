package com.stopsmoke.kekkek.firestorage.dao

import com.stopsmoke.kekkek.firestorage.model.StorageDeleteResult
import com.stopsmoke.kekkek.firestorage.model.StorageUploadResult
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface StorageDao {

    fun uploadFile(
        inputStream: InputStream,
        path: String,
    ): Flow<StorageUploadResult>

    fun deleteFile(path: String): Flow<StorageDeleteResult>
}