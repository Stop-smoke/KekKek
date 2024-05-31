package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow

interface PostDao {

    fun getPost(
        previousItem: PostEntity? = null,
        limit: Long = 30,
    ): Flow<PagingData<PostEntity>>

    suspend fun addPost(postEntity: PostEntity)

    suspend fun updateOrInsertPost(postEntity: PostEntity)

    suspend fun deletePost(postId: String)
}