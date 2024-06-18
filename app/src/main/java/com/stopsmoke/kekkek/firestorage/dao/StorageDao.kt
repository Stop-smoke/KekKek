package com.stopsmoke.kekkek.firestorage.dao

import com.stopsmoke.kekkek.firestorage.model.StorageDeleteResult
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface StorageDao {

    /**
     *  [inputStream] 사진 업로드 InputStream 넣습니다
     *  [path] 저장할 공간입니다
     *  return String 웹 HTTP URL 형태로 사진에 접근 가능한 형태로 반환합니다
     */

    suspend fun uploadFile(
        inputStream: InputStream,
        path: String,
    ): String

    fun deleteFile(path: String): Flow<StorageDeleteResult>
}