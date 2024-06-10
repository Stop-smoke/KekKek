package com.stopsmoke.kekkek.firebaseauth

import com.google.firebase.auth.FirebaseAuth
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.common.exception.GuestModeException
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

    suspend fun withdraw(): Result<Unit> {
        var result: Result<Unit> = Result.Loading

        val currentUser = firebaseAuth.currentUser ?: return Result.Error(GuestModeException())

        currentUser.delete()
            .addOnSuccessListener {
                result = Result.Success(Unit)
            }
            .addOnFailureListener {
                result = Result.Error(it)
            }
            .await()

        return result
    }
}