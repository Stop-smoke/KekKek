package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.firestore.COMMENT_COLLECTION
import com.stopsmoke.kekkek.firestore.POST_COLLECTION
import com.stopsmoke.kekkek.firestore.REPLY_COLLECTION
import com.stopsmoke.kekkek.firestore.dao.ReplyDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReplyDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ReplyDao {
    override suspend fun addReply(replyEntity: ReplyEntity) {
        firestore.collection(POST_COLLECTION)
            .document(replyEntity.commentParent!!.postId!!)
            .collection(COMMENT_COLLECTION)
            .document(replyEntity.replyParent!!)
            .collection(REPLY_COLLECTION)
            .document().let { documentReference ->
                documentReference.set(
                    replyEntity.copy(id = documentReference.id)
                )
            }
            .await()

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

    override suspend fun deleteReply(replyEntity: ReplyEntity) {
        firestore.collection(POST_COLLECTION)
            .document(replyEntity.commentParent!!.postId!!)
            .collection(COMMENT_COLLECTION)
            .document(replyEntity.replyParent!!)
            .collection(REPLY_COLLECTION)
            .document(replyEntity.id!!)
            .delete()
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
        items: List<Any>
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
        items: List<Any>
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