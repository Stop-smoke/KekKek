package com.stopsmoke.kekkek.core.firestore.data

import android.util.Log
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.firestore.COMMENT_COLLECTION
import com.stopsmoke.kekkek.core.firestore.POST_COLLECTION
import com.stopsmoke.kekkek.core.firestore.USER_COLLECTION
import com.stopsmoke.kekkek.core.firestore.WITHDRAW_USER_COLLECTION
import com.stopsmoke.kekkek.core.firestore.dao.UserDao
import com.stopsmoke.kekkek.core.firestore.model.ActivitiesEntity
import com.stopsmoke.kekkek.core.firestore.model.UserEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class UserDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : UserDao {

    override fun getUser(uid: String): Flow<UserEntity> {
        return firestore.collection(USER_COLLECTION)
            .document(uid)
            .dataObjects<UserEntity>()
            .map { it ?: UserEntity() }
    }

    override suspend fun getUserDataFormatUser(uid: String): UserEntity? {
        return try {
            val document = firestore.collection(USER_COLLECTION).document(uid).get().await()
            document.toObject<UserEntity>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun setUser(userEntity: UserEntity) {
        try {
            firestore.collection(USER_COLLECTION).document(userEntity.uid!!)
                .set(userEntity)
                .addOnFailureListener { throw it }
                .await()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override suspend fun setUserDataForName(userEntity: UserEntity, name: String) {
        try {
            val querySnapshot = firestore.collection(USER_COLLECTION).document(userEntity.uid!!)
                .get()
                .await()

            val setUserItem = querySnapshot.toObject<UserEntity>()?.copy(name = name)

            if (setUserItem != null) {
                firestore.collection(USER_COLLECTION).document(userEntity.uid)
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
            val querySnapshot = firestore.collection(USER_COLLECTION).document(userEntity.uid!!)
                .get()
                .await()

            val setUserItem = querySnapshot.toObject<UserEntity>()?.copy(introduction = introduction)

            if (setUserItem != null) {
                firestore.collection(USER_COLLECTION).document(userEntity.uid)
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

    override suspend fun updateUser(uid: String, map: Map<String, Any>) {
        firestore.collection(USER_COLLECTION).document(uid)
            .update(map)
            .await()
    }

    override suspend fun startQuitSmokingTimer(uid: String): Result<Unit> {
        var result: Result<Unit> = Result.Loading

        firestore.collection(USER_COLLECTION)
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

        firestore.collection(USER_COLLECTION)
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
            val querySnapshot = firestore.collection(USER_COLLECTION)
                .whereEqualTo("name", name)
                .get()
                .await()

            querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getActivities(uid: String): Flow<ActivitiesEntity> = callbackFlow {
        val postQuery = firestore.collection(POST_COLLECTION)
            .whereEqualTo("written.uid", uid)
            .count()
            .get(AggregateSource.SERVER)
            .await()

        val commentQuery = firestore.collectionGroup(COMMENT_COLLECTION)
            .whereEqualTo("written.uid", uid)
            .count()
            .get(AggregateSource.SERVER)
            .await()

        val bookmarkQuery = firestore.collection(POST_COLLECTION)
            .whereArrayContains("bookmark_user", uid)
            .count()
            .get(AggregateSource.SERVER)
            .await()

        send(
            ActivitiesEntity(
                postQuery.count,
                commentQuery.count,
                bookmarkQuery.count
            )
        )
        awaitClose()
    }

    override suspend fun getAllUserData(): List<UserEntity> {
        return try {
            val querySnapshot = firestore.collection(USER_COLLECTION).get().await()
            querySnapshot.toObjects<UserEntity>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun withdraw(uid: String) {
        firestore.collection(WITHDRAW_USER_COLLECTION)
            .add(mapOf("uid" to uid))
            .await()
    }

    override suspend fun updatePushServiceToken(uid: String, token: String) {
        firestore.collection(USER_COLLECTION)
            .document(uid)
            .update(mapOf("fcm_token" to token))
            .await()
    }
}