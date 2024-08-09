package com.stopsmoke.kekkek.core.domain.usecase

import com.stopsmoke.kekkek.core.data.mapper.emptyHistory
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.model.UserConfig
import com.stopsmoke.kekkek.core.domain.model.UserRole
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        uid: String,
        name: String,
        dailyCigarettesSmoked: Int,
        packCigaretteCount: Int,
        packPrice: Int
    ) {
        require(uid.isNotBlank())

        val user = User(
            uid = uid,
            name = name,
            profileImage = ProfileImage.Default,
            ranking = Long.MAX_VALUE,
            userConfig = UserConfig(
                dailyCigarettesSmoked = dailyCigarettesSmoked,
                packCigaretteCount = packCigaretteCount,
                packPrice = packPrice,
                birthDate = LocalDateTime.now()
            ),
            history = emptyHistory(),
            role = UserRole.USER
        )
        userRepository.setUserData(user)
    }
}