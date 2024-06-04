package com.stopsmoke.kekkek.storage.data

import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.storage.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class UserMemoryStorage @Inject constructor() : UserDao {

    private val user: MutableStateFlow<User> = MutableStateFlow(
        value = User(
            uid = "default",
            name = "name",
            location = null,
            profileImage = ProfileImage.Default
        )
    )

    override fun getUserData(): Flow<User> {
        return user.asStateFlow()
    }

    override suspend fun setUserData(user: User) {
        this.user.emit(user)
    }
}