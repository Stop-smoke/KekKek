package com.agvber.kekkek.core.domain.usecase

import com.agvber.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke()  = userRepository.getUserData()
}