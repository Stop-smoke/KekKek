package com.stopsmoke.kekkek.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireStoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    fun getUser(userId: String): Flow<Result<UserEntity>> = callbackFlow {

        val snapshotListener = firestore.collection(USER_COLLECTION)
            .document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySendBlocking(Result.failure(error))
                    return@addSnapshotListener
                }
                val user = value?.toObject(UserEntity::class.java) ?: return@addSnapshotListener
                trySendBlocking(Result.success(user))
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    suspend fun setUser(userEntity: UserEntity) {
        firestore.collection(USER_COLLECTION).document(userEntity.uid!!)
            .set(userEntity)
            .addOnFailureListener { throw it }
            .await()
    }


    companion object {
        private const val USER_COLLECTION = "user"
    }
}