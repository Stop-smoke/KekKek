package com.stopsmoke.kekkek.core.domain.repository

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.Activities
import com.stopsmoke.kekkek.core.domain.model.User
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface UserRepository {

    suspend fun setProfileImage(imageInputStream: InputStream)

    /**
     * 다른 유저 정보를 가져옴
     */

    fun getUserData(uid: String): Flow<User>

    /**
     * 자신의 유저 정보를 가져옴
     */
    fun getUserData(): Flow<User>

    suspend fun setUserData(user: User)

    suspend fun setUserName(name: String)

    suspend fun setUserIntroduction(introduction: String)

    /**
     *  금연을 시작 했을때 서버에 시간을 저장 하는 함수
     */

    suspend fun updateUserData(map: Map<String, Any>) // user 정보 업데이트

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

    /**
     * 앱 처음 실행 체크 함수
     */
    fun isFirstRunning(): Flow<Boolean>

    suspend fun logout()

    /**
     * 회원 탈퇴
     */

    suspend fun withdraw()

    suspend fun nameDuplicateInspection(name: String): Boolean

    fun getActivities(): Flow<Activities>

    fun getActivities(userID: String): Flow<Activities>

    suspend fun getAllUserData(): List<User>
}