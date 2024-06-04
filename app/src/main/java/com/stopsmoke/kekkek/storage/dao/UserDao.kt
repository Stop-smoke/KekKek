package com.stopsmoke.kekkek.storage.dao

import com.stopsmoke.kekkek.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserDao {

    fun getUserData(): Flow<User>

    suspend fun setUserData(user: User)
}