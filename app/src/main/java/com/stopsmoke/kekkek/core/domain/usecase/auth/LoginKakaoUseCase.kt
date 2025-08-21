package com.stopsmoke.kekkek.core.domain.usecase.auth

import com.stopsmoke.kekkek.core.domain.repository.AuthenticationRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class LoginKakaoUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() {
        authenticationRepository.loginKakao()
        userRepository.setOnboardingComplete(true)
    }
}