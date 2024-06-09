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

    /**
     *  금연을 시작 했을때 서버에 시간을 저장 하는 함수
     */

    suspend fun startQuitSmokingTimer(): Result<Unit>

    /**
     *  금연을 실패 했을때 서버에 시간을 저장 하는 함수
     */

    suspend fun stopQuitSmokingTimer(): Result<Unit>

    /**
     *  온보딩 체크 함수
     */
    fun isOnboardingComplete(): Flow<Boolean>

    /**
     * 온보딩 설정 함수
     */
    suspend fun setOnboardingComplete(complete: Boolean)
}