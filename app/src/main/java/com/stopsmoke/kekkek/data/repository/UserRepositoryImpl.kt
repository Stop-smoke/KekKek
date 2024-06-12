package com.stopsmoke.kekkek.data.repository

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.toEntity
import com.stopsmoke.kekkek.data.mapper.toExternalModel
import com.stopsmoke.kekkek.data.utils.BitmapCompressor
import com.stopsmoke.kekkek.datastore.PreferencesDataSource
import com.stopsmoke.kekkek.domain.model.ProfileImageUploadResult
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.firebaseauth.AuthenticationDataSource
import com.stopsmoke.kekkek.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.firestorage.model.StorageUploadResult
import com.stopsmoke.kekkek.firestore.dao.UserDao
import com.stopsmoke.kekkek.firestore.model.UserEntity
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
    coroutineScope: CoroutineScope,
    private val authDataSource: AuthenticationDataSource,
) : UserRepository {

    private val user: MutableStateFlow<User> = MutableStateFlow(User.Guest)

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

    override fun setProfileImage(
        imageInputStream: InputStream,
        userId: String,
    ): Flow<ProfileImageUploadResult> {
        val bitmap = BitmapCompressor(imageInputStream)

        return storageDao.uploadFile(
            inputStream = bitmap.getCompressedFile().inputStream(),
            path = "users/$userId/profile_image.jpeg"
        )
            .map {
                when (it) {
                    is StorageUploadResult.Success -> {
                        userDao.setUser(UserEntity(profileImageUrl = it.imageUrl))
                        ProfileImageUploadResult.Success
                    }

                    is StorageUploadResult.Progress -> {
                        ProfileImageUploadResult.Progress
                    }

                    is StorageUploadResult.Error -> {
                        ProfileImageUploadResult.Error(it.exception)
                    }
                }
            }
            .onEach {
                bitmap.deleteOnExit()
            }
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
           return  if (userEntity != null) {
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

    override suspend fun updateUserData(user: User.Registered) {
        userDao.updateUser(user.toEntity())
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

    override fun logout() {
        authDataSource.logout()
    }

    override suspend fun withdraw(): Result<Unit> {
        return authDataSource.withdraw()
    }

}