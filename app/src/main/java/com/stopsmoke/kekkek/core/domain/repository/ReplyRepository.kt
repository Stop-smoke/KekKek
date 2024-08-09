package com.stopsmoke.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.core.domain.model.Reply
import kotlinx.coroutines.flow.Flow

interface ReplyRepository {

    /**
     * reply id 값을 리턴함
     */
    suspend fun addReply(reply: Reply): String

    fun getReply(postId: String, commentId: String, replyId: String) : Flow<Reply>

    fun getReply(commentId: String): Flow<PagingData<Reply>>

    suspend fun deleteReply(postId: String, commentId: String, replyId: String)

    suspend fun updateReply(reply: Reply)

    suspend fun appendReplyLike(postId: String, commentId: String, replyId: String)

    suspend fun removeReplyLike(postId: String, commentId: String, replyId: String)
}