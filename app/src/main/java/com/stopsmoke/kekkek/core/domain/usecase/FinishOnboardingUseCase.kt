package com.stopsmoke.kekkek.core.domain.usecase

import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class FinishOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository
)  {

    suspend operator fun invoke() {
        userRepository.setOnboardingComplete(true)
    }
}