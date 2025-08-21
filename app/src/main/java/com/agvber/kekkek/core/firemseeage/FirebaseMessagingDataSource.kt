package com.agvber.kekkek.core.firemseeage

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMessagingDataSource @Inject constructor(
    private val messaging: FirebaseMessaging
) {

    suspend fun getToken(): String = messaging.token.await()
}