package com.stopsmoke.kekkek.firestore.dao

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {
    fun getUser(uid: String): Flow<UserEntity>

    suspend fun setUser(userEntity: UserEntity)

    suspend fun startQuitSmokingTimer(uid: String): Result<Unit>

    suspend fun stopQuitSmokingTimer(uid: String): Result<Unit>
}