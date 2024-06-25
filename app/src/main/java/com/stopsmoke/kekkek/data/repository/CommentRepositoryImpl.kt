package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.data.mapper.toEntity
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.firestore.dao.CommentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentDao: CommentDao
) : CommentRepository {
    override fun getCommentItems(commentFilter: CommentFilter): Flow<PagingData<Comment>> {
        return when (commentFilter) {
            is CommentFilter.Post -> {
                commentDao.getComment(commentFilter.postId).map { pagingData ->
                    pagingData.map {
                        it.asExternalModel()
                    }
                }
            }

            is CommentFilter.User -> {
                commentDao.getMyCommentItems(commentFilter.uid).map { pagingData ->
                    pagingData.map {
                        it.asExternalModel()
                    }
                }
            }
        }
    }

    override fun getCommentItems(commentIdList: List<String>): Result<Flow<PagingData<Comment>>> =
        try {
            commentDao.getCommentItems(commentIdList)
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


    override suspend fun addCommentItem(comment: Comment): Result<Unit> {
        return try {
            commentDao.addComment(comment.toEntity())
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
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

    override suspend fun getComment(postId: String, commentId: String): Flow<Comment> {
        return commentDao.getComment(postId, commentId).map { commentEntity ->
            commentEntity.asExternalModel()
        }
    }
}