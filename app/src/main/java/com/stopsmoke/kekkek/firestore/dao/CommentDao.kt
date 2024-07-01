package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import kotlinx.coroutines.flow.Flow

interface CommentDao {

    fun getComment(postId: String): Flow<PagingData<CommentEntity>>

    fun getMyCommentItems(uid: String): Flow<PagingData<CommentEntity>>

    fun getCommentItems(commentIdList: List<String>): Flow<PagingData<CommentEntity>>

    /**
     * firestore document ID 값을 리턴합니다
     */
    suspend fun addComment(commentEntity: CommentEntity): String

    suspend fun setCommentItem(commentEntity: CommentEntity)

    suspend fun updateOrInsertComment(commentEntity: CommentEntity)

    suspend fun deleteComment(postId: String, commentId: String)

    fun getCommentCount(postId: String): Flow<Long>

    fun getComment(postId: String, commentId: String): Flow<CommentEntity>

    suspend fun appendItemList(
        postId: String,
        commentId: String,
        field: String,
        items: Any,
    )

    suspend fun removeItemList(
        postId: String,
        commentId: String,
        field: String,
        items: Any,
    )
}