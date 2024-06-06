package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import kotlinx.coroutines.flow.Flow

interface CommentDao {

    fun getComment(postId: String): Flow<PagingData<CommentEntity>>

    fun getMyCommentItems(uid: String): Flow<PagingData<CommentEntity>>

    suspend fun addComment(commentEntity: CommentEntity)

    suspend fun updateOrInsertComment(commentEntity: CommentEntity)

    suspend fun deleteComment(commentId: String)

}