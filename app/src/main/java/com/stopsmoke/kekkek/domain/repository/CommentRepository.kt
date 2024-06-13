package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCommentItems(commentFilter: CommentFilter): Result<Flow<PagingData<Comment>>>

    suspend fun addCommentItem(comment: Comment): Result<Unit>

    fun getCommentItems(commentIdList: List<String>): Result<Flow<PagingData<Comment>>>

    suspend fun insertOrReplaceCommentItem(comment: Comment): Result<Unit>

   suspend fun deleteCommentItem(commentId: String): Result<Unit>

   fun getCommentCount(postId: String): Result<Flow<Long>>
}