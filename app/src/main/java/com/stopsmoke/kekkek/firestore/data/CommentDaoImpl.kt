package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.firestore.dao.CommentDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CommentDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : CommentDao {

    override fun getComment(
        previousItem: PostEntity?,
        limit: Long,
    ): Flow<PagingData<CommentEntity>> {
        return Pager(
            config = PagingConfig(limit.toInt())
        ) {
            FireStorePagingSource(
                query = firestore.collection(COLLECTION)
                    .orderBy("date_time", Query.Direction.DESCENDING),
                limit = limit,
                clazz = CommentEntity::class.java
            )
        }
            .flow
    }

    override suspend fun addComment(commentEntity: CommentEntity) {
        firestore
            .collection(COLLECTION)
            .document().let { documentReference ->
                documentReference.set(
                    commentEntity.copy(id = documentReference.id)
                )
            }
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun updateOrInsertComment(commentEntity: CommentEntity) {
        firestore
            .collection(COLLECTION)
            .document(commentEntity.id!!)
            .set(commentEntity)
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun deleteComment(commentId: String) {
        firestore
            .collection(COLLECTION)
            .document(commentId)
            .delete()
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    companion object {
        private const val COLLECTION = "comment"
    }
}