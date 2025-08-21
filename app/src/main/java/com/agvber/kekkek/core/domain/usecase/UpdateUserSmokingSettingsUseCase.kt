package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserSmokingSettingsUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {

    suspend operator fun invoke(
        dailyCigarettesSmoked: Int,
        packCigaretteCount: Int,
        packPrice: Int
    ) {
        userRepository.updateUserData(
            mapOf(
                "user_config" to mapOf(
                    "daily_cigarettes_smoked" to dailyCigarettesSmoked,
                    "pack_cigarette_count" to packCigaretteCount,
                    "pack_price" to packPrice
                )
            )
        )
    }
}