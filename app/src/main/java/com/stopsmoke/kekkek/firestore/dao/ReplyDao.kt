package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import kotlinx.coroutines.flow.Flow

interface ReplyDao {
    suspend fun addReply(replyEntity: ReplyEntity)

    suspend fun getReply(postId: String, commentId: String): Flow<PagingData<ReplyEntity>>
}