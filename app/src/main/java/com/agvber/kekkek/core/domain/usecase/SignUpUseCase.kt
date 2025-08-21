package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.data.mapper.emptyHistory
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.model.UserConfig
import com.agvber.kekkek.core.domain.model.UserRole
import com.agvber.kekkek.core.domain.repository.UserRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        name: String,
        dailyCigarettesSmoked: Int,
        packCigaretteCount: Int,
        packPrice: Int
    ) {
        val user = User(
            uid = "",
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