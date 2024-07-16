package com.stopsmoke.kekkek.core.domain.usecase

import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    operator fun invoke()  = userRepository.getUserData()
}