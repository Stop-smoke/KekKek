package com.stopsmoke.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.PostEdit
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface PostRepository {
    fun getPost(category: PostCategory = PostCategory.ALL): Flow<PagingData<Post>>

    fun getPostForWrittenUid(writtenUid: String): Result<Flow<PagingData<Post>>>

    fun getBookmark(postIdList: List<String>): Result<Flow<PagingData<Post>>>

    fun getPost(uid: String): Flow<PagingData<Post>>

    fun getPostItem(postId: String): Flow<Post>

    suspend fun addPost(post: PostEdit): Result<Unit>

    suspend fun addPost(post: PostEdit, inputStream: InputStream)

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun editPost(post: Post)

    suspend fun editPost(post: Post, inputStream: InputStream)

    fun getTopNotice(limit: Long): Flow<List<Post>>

    suspend fun getPopularPostList(): Flow<List<Post>>

    suspend fun getPopularPostListNonPeriod(): Flow<List<Post>>

    suspend fun getPostForPostId(postId: String): Post

    suspend fun addViews(postId: String): Result<Unit>

    suspend fun addLikeToPost(postId: String): Result<Unit>

    suspend fun deleteLikeToPost(postId: String): Result<Unit>

    suspend fun addBookmark(postId: String): Result<Unit>

    suspend fun deleteBookmark(postId: String): Result<Unit>

    suspend fun setImage(inputStream: InputStream, path: String)
}