package com.agvber.kekkek.core.firebaseauth

import com.google.firebase.auth.FirebaseAuth
import com.agvber.kekkek.common.exception.GuestModeException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) {

    fun getUid() = callbackFlow<String?> {

        val authStateListener = FirebaseAuth.AuthStateListener {
            trySend(it.uid)
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun withdraw() {
        val currentUser = firebaseAuth.currentUser ?: throw GuestModeException()
        currentUser.delete().await()
    }
}