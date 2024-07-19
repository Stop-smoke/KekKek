package com.stopsmoke.kekkek.core.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.core.firestore.model.ReplyEntity
import kotlinx.coroutines.flow.Flow

interface ReplyDao {

    /**
     * reply id 값을 리턴함
     */

    suspend fun addReply(replyEntity: ReplyEntity) : String

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