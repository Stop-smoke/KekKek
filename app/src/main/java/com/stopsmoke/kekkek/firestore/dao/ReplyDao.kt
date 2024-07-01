package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import kotlinx.coroutines.flow.Flow

interface ReplyDao {
    suspend fun addReply(replyEntity: ReplyEntity)

    suspend fun getReply(commentId: String): Flow<PagingData<ReplyEntity>>

    suspend fun deleteReply(replyEntity: ReplyEntity)

    suspend fun updateReply(replyEntity: ReplyEntity)

    suspend fun appendItemList(
        postId: String,
        commentId: String,
        replyId: String,
        field: String,
        items: List<Any>
    )

    suspend fun removeItemList(
        postId: String,
        commentId: String,
        replyId: String,
        field: String,
        items: List<Any>
    )
}