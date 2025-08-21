package com.agvber.kekkek.core.domain.usecase.auth

import com.agvber.kekkek.core.domain.repository.AuthenticationRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class LoginGoogleUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() {
        authenticationRepository.loginGoogle()
        userRepository.setOnboardingComplete(true)
    }
}