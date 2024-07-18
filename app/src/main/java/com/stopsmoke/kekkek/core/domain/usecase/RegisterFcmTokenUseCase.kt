package com.stopsmoke.kekkek.core.domain.usecase

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke() {
        userRepository.updateUserData(
            mapOf("fcm_token" to Firebase.messaging.token.await())
        )
    }
}