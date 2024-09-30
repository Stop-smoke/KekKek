package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class UpdateCigaretteAddictionTestResultUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(result: String) {
        userRepository.updateUserData(
            mapOf(
                "cigarette_addiction_test_result" to result
            )
        )
    }
}