package com.agvber.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.agvber.kekkek.common.Result
import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.core.domain.model.CommentFilter
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCommentItems(commentFilter: CommentFilter): Flow<PagingData<Comment>>

    suspend fun addCommentItem(comment: Comment): String

    suspend fun deleteCommentItem(postId: String, commentId: String): Result<Unit>

    fun getComment(postId: String, commentId: String): Flow<Comment>

    suspend fun addCommentLike(postId: String, commentId: String)

    suspend fun removeCommentLike(postId: String, commentId: String)
}