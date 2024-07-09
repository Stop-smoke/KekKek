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
import com.stopsmoke.kekkek.core.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.core.firestore.dao.PostDao
import com.stopsmoke.kekkek.core.firestore.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
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
) : UserRepository {

    private val user: MutableStateFlow<User> = MutableStateFlow(
        User.Guest)

    init {
        coroutineScope.launch {
            authDataSource.getUid().collectLatest {
                observeUser(it)
            }
        }
    }

    private suspend fun observeUser(uid: String?) {
        if (uid == null) {
            user.emit(User.Guest)
            return
        }

        kotlin.runCatching {
            userDao.getUser(uid).collectLatest {
                user.emit(it.toExternalModel())
            }
        }
            .onFailure {
                user.emit(User.Error(it))
            }
    }

    override suspend fun setProfileImage(
        imageInputStream: InputStream
    ) {
        val user = user.value as? User.Registered ?: throw GuestModeException()

        val bitmap = BitmapCompressor(imageInputStream)
        val uploadUrl = storageDao.uploadFile(
            inputStream = bitmap.getCompressedFile().inputStream(),
            path = "users/${user.uid}/profile_image.jpeg"
        )
        userDao.updateUser(user.uid, mapOf("profile_image_url" to uploadUrl))
        postDao.setProfileImage(user.uid, uploadUrl)
    }

    override fun getUserData(uid: String): Result<Flow<User.Registered>> {
        return try {
            userDao.getUser(uid).map { user ->
                user.toExternalModel()
            }
                .let {
                    Result.Success(it)
                }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getUserData(): Flow<User> {
        return user
    }

    override suspend fun getUserDataFormatUser(uid: String): User {
        return try {
            val userEntity = userDao.getUserDataFormatUser(uid)
            return if (userEntity != null) {
                userEntity.toExternalModel()
            } else {
                User.Guest
            }
        } catch (e: Exception) {
            e.printStackTrace()
            User.Error(e)
        }
    }

    override suspend fun setUserData(user: User.Registered) {
        userDao.setUser(user.toEntity())
    }

    override suspend fun setUserDataForName(user: User.Registered, name: String) {
        userDao.setUserDataForName(user.toEntity(), name)
        postDao.setUserDataForName(user.toEntity(), name)
    }

    override suspend fun setUserDataForIntroduction(user: User.Registered, introduction: String) {
        userDao.setUserDataForIntroduction(user.toEntity(), introduction)
    }

    override suspend fun updateUserData(map: Map<String, Any>) {
        val user = user.value as? User.Registered ?: throw GuestModeException()
        userDao.updateUser(user.uid, map)
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
        user.emit(User.Guest)
        preferencesDataSource.clearAll()
    }

    override suspend fun withdraw() {
        (user.value as? User.Registered)?.let {
            userDao.withdraw(it.uid)
        }
        user.emit(User.Guest)
        preferencesDataSource.clearAll()
    }

    override suspend fun nameDuplicateInspection(name: String): Boolean {
        return userDao.nameDuplicateInspection(name)
    }

    override fun getActivities(): Flow<Activities> {
        return userDao.getActivities((user.value as User.Registered).uid)
            .map {
                Activities(
                    it.postCount,
                    it.commentCount,
                    it.bookmarkCount
                )
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