package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.PostEdit
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPost(category: PostCategory = PostCategory.ALL): Flow<PagingData<Post>>

    fun getPostForWrittenUid(writtenUid: String): Result<Flow<PagingData<Post>>>

    fun getBookmark(postIdList: List<String>): Result<Flow<PagingData<Post>>>

    fun getPost(uid: String): Flow<PagingData<Post>>

    fun getPostItem(postId: String): Flow<List<Post>>

    suspend fun addPost(post: PostEdit): Result<Unit>

    suspend fun deletePost(postId: String): Result<Unit>

    suspend fun editPost(post: Post): Result<Unit>

    suspend fun getTopPopularItems(): Flow<List<Post>>

    suspend fun getTopNotice(): Post

    suspend fun getPopularPostList(): List<Post>

    suspend fun getPostForPostId(postId: String): Post

    suspend fun addViews(postId: String): Result<Unit>

    suspend fun addLikeToPost(postId: String): Result<Unit>

    suspend fun deleteLikeToPost(postId: String): Result<Unit>

    suspend fun addBookmark(postId: String): Result<Unit>

    suspend fun deleteBookmark(postId: String): Result<Unit>

}