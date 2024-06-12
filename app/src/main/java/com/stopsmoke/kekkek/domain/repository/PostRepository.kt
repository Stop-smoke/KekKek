package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.PostWrite
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(category: PostCategory = PostCategory.UNKNOWN): Result<Flow<PagingData<Post>>>

    fun getPostForWrittenUid(writtenUid: String): Result<Flow<PagingData<Post>>>

    fun getBookmark(postIdList: List<String>): Result<Flow<PagingData<Post>>>

    suspend fun addPost(post: PostWrite): Result<Unit>

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun editPost(post: PostWrite): Result<Unit>

    suspend fun getTopPopularItems(): List<Post>

    suspend fun getTopNotice(): Post

    suspend fun getPopularPostList(): List<Post>
}