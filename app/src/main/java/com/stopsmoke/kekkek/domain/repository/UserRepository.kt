package com.stopsmoke.kekkek.domain.repository

import com.stopsmoke.kekkek.domain.model.ProfileImageUploadResult
import com.stopsmoke.kekkek.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface UserRepository {

    fun setProfileImage(
        imageInputStream: InputStream,
        userId: String,
    ): Flow<ProfileImageUploadResult>

    suspend fun setUserData(user: User)
}