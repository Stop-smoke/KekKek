package com.stopsmoke.kekkek.core.data.repository

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.common.exception.GuestModeException
import com.stopsmoke.kekkek.core.data.mapper.toEntity
import com.stopsmoke.kekkek.core.data.mapper.toExternalModel
import com.stopsmoke.kekkek.core.data.utils.BitmapCompressor
import com.stopsmoke.kekkek.core.datastore.PreferencesDataSource
import com.stopsmoke.kekkek.core.domain.model.Activities
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.firebaseauth.AuthenticationDataSource
import com.stopsmoke.kekkek.core.firemseeage.FirebaseMessagingDataSource
import com.stopsmoke.kekkek.core.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.core.firestore.dao.PostDao
import com.stopsmoke.kekkek.core.firestore.dao.UserDao
import com.stopsmoke.kekkek.core.firestore.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserRepositoryImpl @Inject constructor(
    private val storageDao: StorageDao,
    private val userDao: UserDao,
    private val preferencesDataSource: PreferencesDataSource,
    private val postDao: PostDao,
    coroutineScope: CoroutineScope,
    private val authDataSource: AuthenticationDataSource,
    private val messaging: FirebaseMessagingDataSource,
) : UserRepository {

    private val user: MutableStateFlow<User?> = MutableStateFlow(null)

    init {
        coroutineScope.launch {
            try {
                authDataSource.getUid().collectLatest {
                    if (it != null) {
                        handleUserData(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                user.emit(null)
            }
        }
    }

    private suspend fun handleUserData(uid: String) {
        kotlin.runCatching {
            userDao.getUser(uid).collectLatest {
                checkPushServiceToken(it)
                user.emit(it.toExternalModel())
            }
        }
    }

    private fun getUser(): User = user.value ?: throw GuestModeException()

    private suspend fun checkPushServiceToken(user: UserEntity) {
        val token = messaging.getToken()
        if (token == user.fcmToken || user.uid == null) {
            return
        }
        userDao.updatePushServiceToken(user.uid, token)
    }

    override suspend fun setProfileImage(
        imageInputStream: InputStream,
    ) {
        val user = user.value ?: throw GuestModeException()

        val bitmap = BitmapCompressor(imageInputStream)
        val uploadUrl = storageDao.uploadFile(
            inputStream = bitmap.getCompressedFile().inputStream(),
            path = "users/${user.uid}/profile_image.jpeg"
        )
        userDao.updateUser(user.uid, mapOf("profile_image_url" to uploadUrl))
        postDao.setProfileImage(user.uid, uploadUrl)
    }

    override fun getUserData(uid: String): Flow<User> = try {
        userDao.getUser(uid).map { user ->
            user.toExternalModel()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        flow { throw e }
    }

    override fun getUserData(): Flow<User> {
        return user.map { it ?: throw GuestModeException() }
    }

    override suspend fun setUserData(user: User) {
        userDao.setUser(user.toEntity())
    }

    override suspend fun setUserName(name: String) {
        userDao.setUserName(getUser().uid, name)
    }

    override suspend fun setUserIntroduction(introduction: String) {
        userDao.setUserIntroduction(getUser().uid, introduction)
    }

    override suspend fun updateUserData(map: Map<String, Any>) {
        userDao.updateUser(getUser().uid, map)
    }

    override suspend fun startQuitSmokingTimer(): Result<Unit> {
        return userDao.startQuitSmokingTimer("default")
    }

    override suspend fun stopQuitSmokingTimer(): Result<Unit> {
        return userDao.stopQuitSmokingTimer("default")
    }

    override fun isOnboardingComplete(): Flow<Boolean> {
        return preferencesDataSource.isOnboardingComplete()
    }

    override suspend fun setOnboardingComplete(complete: Boolean) {
        preferencesDataSource.setOnboardingComplete(complete)
    }

    override fun isFirstRunning(): Flow<Boolean> =
        preferencesDataSource.isFirstRunning()
            .onEach {
                if (it) {
                    preferencesDataSource.setFirstRunning(false)
                }
            }

    override suspend fun logout() {
        authDataSource.logout()
        clearApp()
    }

    override suspend fun withdraw() {
        userDao.withdraw(getUser().uid)
        clearApp()
    }

    private suspend fun clearApp() {
//        user.emit(null)
        preferencesDataSource.clearAll()
    }

    override suspend fun nameDuplicateInspection(name: String): Boolean {
        return userDao.nameDuplicateInspection(name)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getActivities(): Flow<Activities> {
        return user.flatMapLatest { user ->
            if (user == null) {
                throw GuestModeException()
            }

            userDao.getActivities(user.uid)
                .map {
                    Activities(
                        it.postCount,
                        it.commentCount,
                        it.bookmarkCount
                    )
                }
        }
    }

    override fun getActivities(userID: String): Flow<Activities> {
        return userDao.getActivities(userID)
            .map {
                Activities(
                    it.postCount,
                    it.commentCount,
                    it.bookmarkCount
                )
            }
    }

    override suspend fun getAllUserData(): List<User> {
        return userDao.getAllUserData().map {
            it.toExternalModel()
        }
    }
}