package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.data.mapper.toEntity
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.PostEdit
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.domain.model.toRequestString
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.firestore.dao.PostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.io.InputStream
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val userRepository: UserRepository,
    private val storageDao: StorageDao
) : PostRepository {
    override fun getPost(category: PostCategory): Flow<PagingData<Post>> {
        return postDao.getPost(category = category.toRequestString())
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }
    }

    override fun getPost(uid: String): Flow<PagingData<Post>> {
        return postDao.getPostUserFilter(uid).map { pagingData ->
            pagingData.map {
                it.asExternalModel()
            }
        }
    }

    override fun getPostForWrittenUid(writtenUid: String): Result<Flow<PagingData<Post>>> = try {
        postDao.getPostForWrittenUid(writtenUid = writtenUid)
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }.let {
                Result.Success(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(e)
    }

    override fun getBookmark(postIdList: List<String>): Result<Flow<PagingData<Post>>> = try {
        postDao.getBookmark(postIdList)
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }.let {
                Result.Success(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(e)
    }

    override fun getPostItem(postId: String): Flow<Post> {
        return postDao.getPostItem(postId)
            .map {
                it.asExternalModel()
            }
            .onStart {
                postDao.addViews(postId)
            }
    }

    override suspend fun addPost(post: PostEdit): Result<Unit> =
        try {
            val user = (userRepository.getUserData().first() as User.Registered)
            val written = Written(
                uid = user.uid,
                name = user.name,
                profileImage = user.profileImage,
                ranking = user.ranking
            )
            postDao.addPost(post.toEntity(written))
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

    override suspend fun addPost(post: PostEdit, inputStream: InputStream) {
        val user = (userRepository.getUserData().first() as User.Registered)
        val written = Written(
            uid = user.uid,
            name = user.name,
            profileImage = user.profileImage,
            ranking = user.ranking,
        )

        postDao.addPost(post.toEntity(written), inputStream)
    }

    override suspend fun deletePost(postId: String): Result<Unit> {
        return try {
            postDao.deletePost(postId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun editPost(post: Post): Result<Unit> {
        return try {
            postDao.editPost(post.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    override suspend fun editPost(post: Post, inputStream: InputStream) {
        postDao.editPost(post.toEntity(), inputStream)
    }

    override suspend fun getTopPopularItems(): Flow<List<Post>> =
        postDao.getPopularPostItems().map {
            it.map { it.asExternalModel() }
        }

    override fun getTopNotice(limit: Long): Flow<List<Post>> =
        postDao.getTopNotice(limit).map { posts ->
            posts.map { it.asExternalModel() }
        }

    override suspend fun getPopularPostList(): List<Post> =
        postDao.getPopularPostList().map {
            it.asExternalModel()
        }

    override suspend fun getPostForPostId(postId: String): Post {
        return postDao.getPostForPostId(postId).asExternalModel()

    }

    override suspend fun addViews(postId: String): Result<Unit> {
        return postDao.addViews(postId)
    }

    override suspend fun addLikeToPost(postId: String): Result<Unit> {
        return try {
            val user = (userRepository.getUserData().first() as User.Registered)
            postDao.addLike(postId, user.uid)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLikeToPost(postId: String): Result<Unit> {
        return try {
            val user = (userRepository.getUserData().first() as User.Registered)
            postDao.deleteLike(postId, user.uid)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addBookmark(postId: String): Result<Unit> {
        return try {
            val user = (userRepository.getUserData().first() as User.Registered)
            postDao.addBookmark(postId, user.uid)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteBookmark(postId: String): Result<Unit> {
        return try {
            val user = (userRepository.getUserData().first() as User.Registered)
            postDao.deleteBookmark(postId, user.uid)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun setImage(inputStream: InputStream, path: String) {
        val uploadUrl = storageDao.uploadFile(inputStream, path)
    }
}