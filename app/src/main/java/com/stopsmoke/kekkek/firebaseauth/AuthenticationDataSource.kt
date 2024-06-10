package com.stopsmoke.kekkek.firebaseauth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthenticationDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
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
}