package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow

interface CommentDao {

    fun getComment(
        previousItem: PostEntity? = null,
        limit: Long = 30,
    ): Flow<PagingData<CommentEntity>>

    suspend fun addComment(commentEntity: CommentEntity)

    suspend fun updateOrInsertComment(commentEntity: CommentEntity)

    suspend fun deleteComment(commentId: String)

}