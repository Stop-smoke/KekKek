package com.stopsmoke.kekkek.firestore.dao

import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {

    /**
     *  자신의 userData 가져오는 함수
     */

    fun getUser(): Flow<UserEntity>

    /**
     *  다른 사람의 userData 가져오는 함수
     */

    fun getUser(uid: String): Flow<UserEntity>

    suspend fun setUser(userEntity: UserEntity)

}