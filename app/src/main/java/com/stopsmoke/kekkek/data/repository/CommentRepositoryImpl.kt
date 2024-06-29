package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.data.mapper.toEntity
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.firestore.dao.CommentDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentDao: CommentDao,
    private val userRepository: UserRepository
) : CommentRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCommentItems(commentFilter: CommentFilter): Flow<PagingData<Comment>> {
        return when (commentFilter) {
            is CommentFilter.Post -> {
                userRepository.getUserData().flatMapLatest { user ->
                    commentDao.getComment(commentFilter.postId).map { pagingData ->
                        pagingData.map {
                            val earliest = it.earliestReply.map { reply ->
                                reply.asExternalModel(reply.likeUser.contains((user as? User.Registered)?.uid))
                            }
                            it.asExternalModel(earliest)
                        }
                    }
                }
            }

            is CommentFilter.User -> {
                userRepository.getUserData().flatMapLatest { user ->
                    commentDao.getMyCommentItems(commentFilter.uid).map { pagingData ->
                        pagingData.map {
                            val earliest = it.earliestReply.map { reply ->
                                reply.asExternalModel(reply.likeUser.contains((user as? User.Registered)?.uid))
                            }
                            it.asExternalModel(earliest)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCommentItems(commentIdList: List<String>): Result<Flow<PagingData<Comment>>> =
        try {
            userRepository.getUserData().flatMapLatest { user ->
                commentDao.getCommentItems(commentIdList)
                    .map { pagingData ->
                        pagingData.map {
                            val earliest = it.earliestReply.map { reply ->
                                reply.asExternalModel(reply.likeUser.contains((user as? User.Registered)?.uid))
                            }
                            it.asExternalModel(earliest)
                        }
                    }
            }
                .let {
                    Result.Success(it)
                }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }


    override suspend fun addCommentItem(comment: Comment) {
        commentDao.addComment(comment.toEntity())
    }

    override suspend fun setCommentItem(comment: Comment): Result<Unit> {
        return try {
            commentDao.setCommentItem(comment.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun insertOrReplaceCommentItem(comment: Comment): Result<Unit> {
        return try {
            Result.Success(commentDao.updateOrInsertComment(comment.toEntity()))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteCommentItem(postId: String, commentId: String): Result<Unit> {
        return try {
            Result.Success(commentDao.deleteComment(postId, commentId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getCommentCount(postId: String): Flow<Long> {
        return commentDao.getCommentCount(postId)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getComment(postId: String, commentId: String): Flow<Comment> {
        return userRepository.getUserData().flatMapLatest { user ->
            commentDao.getComment(postId, commentId).map {
                val earliest = it.earliestReply.map { reply ->
                    reply.asExternalModel(reply.likeUser.contains((user as? User.Registered)?.uid))
                }
                it.asExternalModel(earliest)
            }
        }
    }
}
