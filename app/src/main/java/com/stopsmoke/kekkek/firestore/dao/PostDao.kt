package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow

interface PostDao {

    /**
     * 게시글을 가져오는 함수입니다
     * [category] 공지사항, 인기글 등등 필터를 적용해서 가져올 수 있습니다
     */
    // https://www.notion.so/stopsmoke/enum-PostCategory-c6956f5b008d4185bcd3dfe42dfbc14e?pvs=4
    fun getPost(category: String? = null): Flow<PagingData<PostEntity>>

    fun getPostForWrittenUid(writtenUid: String): Flow<PagingData<PostEntity>>

    fun getBookmark(postIdList: List<String>): Flow<PagingData<PostEntity>>

    fun getPostUserFilter(uid: String): Flow<PagingData<PostEntity>>

    fun getPostItem(postId: String): Flow<List<PostEntity>>

    suspend fun addPost(postEntity: PostEntity)

    suspend fun editPost(postEntity: PostEntity): Result<Unit>

    suspend fun updateOrInsertPost(postEntity: PostEntity)

    suspend fun deletePost(postId: String)

    suspend fun getPopularPostItems(): List<PostEntity>

    suspend fun getTopNotice(): PostEntity

    suspend fun getPopularPostList(): List<PostEntity>

    suspend fun getPostForPostId(postId: String): PostEntity

    fun getCommentCount(postId: String): Flow<Long>

    fun getLikeCount(postId: String): Flow<Long>

    suspend fun addLike(postId: String, uid: String): Result<Unit>

    suspend fun deleteLike(postId: String, uid: String): Result<Unit>

    suspend fun addViews(postId: String): Result<Unit>
}