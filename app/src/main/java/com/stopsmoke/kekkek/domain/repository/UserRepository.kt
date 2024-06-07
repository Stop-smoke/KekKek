package com.stopsmoke.kekkek.domain.repository

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.ProfileImageUploadResult
import com.stopsmoke.kekkek.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface UserRepository {

    fun setProfileImage(
        imageInputStream: InputStream,
        userId: String,
    ): Flow<ProfileImageUploadResult>

    /**
     * 다른 유저 정보를 가져옴
     */

    fun getUserData(uid: String): Result<Flow<User>>

    /**
     * 자신의 유저 정보를 가져옴
     */
    fun getUserData(): Result<Flow<User>>

    suspend fun setUserData(user: User)
}