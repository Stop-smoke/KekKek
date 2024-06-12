package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
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
    override fun getCommentItems(commentFilter: CommentFilter): Result<Flow<PagingData<Comment>>> {
        return try {
            when (commentFilter) {
                is CommentFilter.Me -> {
                    commentDao.getMyCommentItems("default").map { pagingData ->
                        pagingData.map {
                            it.asExternalModel()
                        }
                    }
                }

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
                .let {
                    Result.Success(it)
                }

        } catch (e: Exception) {
            Result.Error(e)
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


    override fun addCommentItem(comment: Comment): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun insertOrReplaceCommentItem(comment: Comment): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteCommentItem(commentId: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}