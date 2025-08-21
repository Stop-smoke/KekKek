package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class FinishOnboardingUseCase @Inject constructor(
    private val userRepository: UserRepository
)  {

    suspend operator fun invoke() {
        userRepository.setOnboardingComplete(true)
    }
}