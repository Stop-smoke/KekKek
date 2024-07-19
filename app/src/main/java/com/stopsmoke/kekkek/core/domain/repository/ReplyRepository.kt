package com.stopsmoke.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.core.domain.model.Reply
import kotlinx.coroutines.flow.Flow

interface ReplyRepository {

    /**
     * reply id 값을 리턴함
     */
    suspend fun addReply(reply: Reply): String

    suspend fun getReply(commentId: String): Flow<PagingData<Reply>>

    suspend fun deleteReply(reply: Reply)

    suspend fun updateReply(reply: Reply)

    suspend fun appendReplyLike(postId: String, commentId: String, replyId: String)

    suspend fun removeReplyLike(postId: String, commentId: String, replyId: String)
}