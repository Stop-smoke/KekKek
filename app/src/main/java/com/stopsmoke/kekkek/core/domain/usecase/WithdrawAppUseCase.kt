package com.stopsmoke.kekkek.core.domain.usecase

import com.stopsmoke.kekkek.core.domain.repository.AuthenticationRepository
import javax.inject.Inject

class WithdrawAppUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {

    suspend operator fun invoke() {
        authenticationRepository.withdraw()
    }
}