package com.stopsmoke.kekkek.firestore.data

import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.firestore.dao.PostDao
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

internal class PostDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : PostDao {
    override fun getPost(
        previousItem: PostEntity?,
        limit: Long,
    ): Flow<Result<List<PostEntity>>> = callbackFlow {
        val snapshotListener = firestore.collection(POST_COLLECTION)
            .orderBy("date_time", Query.Direction.DESCENDING)
            .where(Filter.equalTo("title", ""))
            .startAt()
            .limit(limit)
            .startAt(previousItem)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySendBlocking(Result.failure(error))
                    return@addSnapshotListener
                }

                val result = runCatching {
                    value!!.documents.map { it.toObject(PostEntity::class.java)!! }
                }
                trySendBlocking(result)
            }

        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addPost(postEntity: PostEntity) {
        firestore.collection(POST_COLLECTION).document().let { document ->
            document.set(postEntity.copy(id = document.id))
                .addOnFailureListener { throw it }
                .addOnCanceledListener { throw CancellationException() }
                .await()
        }
    }

    override suspend fun updateOrInsertPost(postEntity: PostEntity) {
        firestore.collection(POST_COLLECTION)
            .document(postEntity.id!!)
            .set(postEntity)
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun deletePost(postId: String) {
        firestore.collection(POST_COLLECTION)
            .document(postId)
            .delete()
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    companion object {
        private const val POST_COLLECTION = "post"
    }
}