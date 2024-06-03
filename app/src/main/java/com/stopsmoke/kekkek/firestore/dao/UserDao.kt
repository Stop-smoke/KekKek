package com.stopsmoke.kekkek.firestore.dao

import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {

    fun getUser(uid: String): Flow<UserEntity>

    suspend fun setUser(userEntity: UserEntity)

}