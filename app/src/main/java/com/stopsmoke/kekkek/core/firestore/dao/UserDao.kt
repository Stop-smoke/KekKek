package com.stopsmoke.kekkek.core.firestore.dao

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.firestore.model.ActivitiesEntity
import com.stopsmoke.kekkek.core.firestore.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {
    fun getUser(uid: String): Flow<UserEntity>

    suspend fun setUser(userEntity: UserEntity)

    suspend fun setUserName(uid: String, name: String)

    suspend fun setUserIntroduction(uid: String, introduction: String)

    suspend fun updateUser(uid: String, map: Map<String, Any>)

    suspend fun startQuitSmokingTimer(uid: String): Result<Unit>

    suspend fun stopQuitSmokingTimer(uid: String): Result<Unit>

    suspend fun nameDuplicateInspection(name: String): Boolean

    fun getActivities(uid: String): Flow<ActivitiesEntity>

    suspend fun getAllUserData(): List<UserEntity>

    suspend fun withdraw(uid: String)

    suspend fun updatePushServiceToken(uid: String, token: String)
}