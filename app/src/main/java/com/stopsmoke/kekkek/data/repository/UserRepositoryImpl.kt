package com.stopsmoke.kekkek.data.repository

import com.stopsmoke.kekkek.data.utils.BitmapCompressor
import com.stopsmoke.kekkek.domain.model.ProfileImageUploadResult
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.firestorage.model.StorageUploadResult
import com.stopsmoke.kekkek.firestore.dao.UserDao
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.io.InputStream
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val storageDao: StorageDao,
    private val userDao: UserDao,
) : UserRepository {

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
}