package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import kotlinx.coroutines.flow.Flow

interface ReplyDao {
    suspend fun addReply(replyEntity: ReplyEntity)

    suspend fun getReply(postId: String, commentId: String): Flow<PagingData<ReplyEntity>>

    suspend fun deleteReply(replyEntity: ReplyEntity)

    suspend fun updateReply(replyEntity: ReplyEntity)
}