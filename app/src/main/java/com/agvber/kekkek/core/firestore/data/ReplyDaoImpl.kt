package com.agvber.kekkek.core.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.agvber.kekkek.core.firestore.COMMENT_COLLECTION
import com.agvber.kekkek.core.firestore.POST_COLLECTION
import com.agvber.kekkek.core.firestore.REPLY_COLLECTION
import com.agvber.kekkek.core.firestore.dao.ReplyDao
import com.agvber.kekkek.core.firestore.mapper.toInit
import com.agvber.kekkek.core.firestore.model.ReplyEntity
import com.agvber.kekkek.core.firestore.pager.FireStorePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReplyDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ReplyDao {
    override suspend fun addReply(replyEntity: ReplyEntity): String {
        var replyId: String

        firestore.collection(POST_COLLECTION)
            .document(replyEntity.commentParent!!.postId!!)
            .collection(COMMENT_COLLECTION)
            .document(replyEntity.replyParent!!)
            .collection(REPLY_COLLECTION)
            .document().let { documentReference ->
                replyId = documentReference.id
                val entity = replyEntity.copy(
                    id = documentReference.id,
                )
                    .toInit()
                documentReference.set(entity)
            }
            .await()
        return replyId
    }

    override fun getReply(
        postId: String,
        commentId: String,
        replyId: String,
    ): Flow<ReplyEntity> {
        return firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .document(commentId)
            .collection(REPLY_COLLECTION)
            .document(replyId)
            .dataObjects<ReplyEntity>()
            .mapNotNull { it }
    }

    override suspend fun getReply(commentId: String): Flow<PagingData<ReplyEntity>> {
        val query = firestore.collectionGroup(REPLY_COLLECTION)
            .whereEqualTo("reply_parent", commentId)
            .orderBy("date_time.created", Query.Direction.ASCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = ReplyEntity::class.java
            )
        }
            .flow
    }

    override suspend fun deleteReply(postId: String, commentId: String, replyId: String) {
        firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .document(commentId)
            .collection(REPLY_COLLECTION)
            .document(replyId)
            .delete()
            .await()
    }

    override suspend fun updateReply(replyEntity: ReplyEntity) {
        firestore.collection(POST_COLLECTION)
            .document(replyEntity.commentParent!!.postId!!)
            .collection(COMMENT_COLLECTION)
            .document(replyEntity.replyParent!!)
            .collection(REPLY_COLLECTION)
            .document(replyEntity.id!!)
            .update(mapOf("like_user" to replyEntity.likeUser))
    }

    override suspend fun appendItemList(
        postId: String,
        commentId: String,
        replyId: String,
        field: String,
        items: List<Any>,
    ) {
        firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .document(commentId)
            .collection(REPLY_COLLECTION)
            .document(replyId)
            .update(field, FieldValue.arrayUnion(items[0]))
            .await()
    }

    override suspend fun removeItemList(
        postId: String,
        commentId: String,
        replyId: String,
        field: String,
        items: List<Any>,
    ) {
        firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .document(commentId)
            .collection(REPLY_COLLECTION)
            .document(replyId)
            .update(field, FieldValue.arrayRemove(items[0]))
            .await()
    }

    companion object {
        private const val PAGE_LIMIT = 30
    }
}