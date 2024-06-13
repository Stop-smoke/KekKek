package com.stopsmoke.kekkek.firestore.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.firestore.dao.UserDao
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class UserDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserDao {

    override fun getUser(uid: String): Flow<UserEntity> {
        return firestore.collection(COLLECTION)
            .document(uid)
            .dataObjects<UserEntity>()
            .map { it ?: UserEntity() }
    }

    override suspend fun getUserDataFormatUser(uid: String): UserEntity? {
        return try {
            val document = firestore.collection(COLLECTION).document(uid).get().await()
            document.toObject<UserEntity>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun setUser(userEntity: UserEntity) {
        firestore.collection(COLLECTION).document(userEntity.uid!!)
            .set(userEntity)
            .addOnFailureListener { throw it }
            .await()
    }

    override suspend fun setUserDataForName(userEntity: UserEntity, name: String) {
        try {
            val querySnapshot = firestore.collection(COLLECTION).document(userEntity.uid!!)
                .get()
                .await()

            val setUserItem = querySnapshot.toObject<UserEntity>()?.copy(name = name)

            if (setUserItem != null) {
                firestore.collection(COLLECTION).document(userEntity.uid!!)
                    .set(setUserItem)
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Failed to set document", exception)
                        throw exception
                    }
                    .await()
            } else {
                Log.e("Firestore", "Failed to convert document to UserEntity or userEntity.uid is null")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Exception occurred", e)
        }
    }

    override suspend fun setUserDataForIntroduction(userEntity: UserEntity, introduction: String) {
        try {
            val querySnapshot = firestore.collection(COLLECTION).document(userEntity.uid!!)
                .get()
                .await()

            val setUserItem = querySnapshot.toObject<UserEntity>()?.copy(introduction = introduction)

            if (setUserItem != null) {
                firestore.collection(COLLECTION).document(userEntity.uid!!)
                    .set(setUserItem)
                    .addOnFailureListener { exception ->
                        Log.e("Firestore", "Failed to set document", exception)
                        throw exception
                    }
                    .await()
            } else {
                Log.e("Firestore", "Failed to convert document to UserEntity or userEntity.uid is null")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Exception occurred", e)
        }
    }

    override suspend fun updateUser(userEntity: UserEntity) {
        firestore.collection(COLLECTION).document(userEntity.uid!!)
            .set(userEntity, SetOptions.merge())
            .addOnFailureListener { throw it }
            .await()
    }

    override suspend fun startQuitSmokingTimer(uid: String): Result<Unit> {
        var result: Result<Unit> = Result.Loading

        firestore.collection(COLLECTION)
            .document(uid)
            .set(UserEntity())
            .addOnSuccessListener {
                result = Result.Success(Unit)
            }
            .addOnFailureListener {
                result = Result.Error(it)
            }
            .await()

        return result
    }

    override suspend fun stopQuitSmokingTimer(uid: String): Result<Unit> {
        var result: Result<Unit> = Result.Loading

        firestore.collection(COLLECTION)
            .document(uid)
            .set(UserEntity())
            .addOnSuccessListener {
                result = Result.Success(Unit)
            }
            .addOnFailureListener {
                result = Result.Error(it)
            }
            .await()

        return result
    }

    override suspend fun nameDuplicateInspection(name: String): Boolean {
        return try {
            val querySnapshot = firestore.collection(COLLECTION)
                .whereEqualTo("name", name)
                .get()
                .await()

            querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    companion object {
        private const val COLLECTION = "user"
    }
}