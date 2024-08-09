package com.stopsmoke.kekkek.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.data.mapper.asExternalModel
import com.stopsmoke.kekkek.core.data.mapper.toEntity
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.CommentFilter
import com.stopsmoke.kekkek.core.domain.repository.CommentRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.firestore.dao.CommentDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentDao: CommentDao,
    private val userRepository: UserRepository,
) : CommentRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCommentItems(commentFilter: CommentFilter): Flow<PagingData<Comment>> {
        return when (commentFilter) {
            is CommentFilter.Post -> {
                userRepository.getUserData().flatMapLatest { user ->
                    commentDao.getComment(commentFilter.postId).map { pagingData ->
                        pagingData.map {
                            val earliest = it.earliestReply.map { reply ->
                                reply.asExternalModel(reply.likeUser.contains(user.uid))
                            }
                            val isLiked = it.likeUser.contains(user.uid)
                            it.asExternalModel(earliest, isLiked)
                        }
                    }
                }
            }

            is CommentFilter.User -> {
                userRepository.getUserData().flatMapLatest { user ->
                    commentDao.getMyCommentItems(commentFilter.uid).map { pagingData ->
                        pagingData.map {
                            val earliest = it.earliestReply.map { reply ->
                                reply.asExternalModel(reply.likeUser.contains(user.uid))
                            }
                            val isLiked = it.likeUser.contains(user.uid)
                            it.asExternalModel(earliest, isLiked)
                        }
                    }
                }
            }
        }
    }


    override suspend fun addCommentItem(comment: Comment): String {
        return commentDao.addComment(comment.toEntity())
    }

    override suspend fun deleteCommentItem(postId: String, commentId: String): Result<Unit> {
        return try {
            Result.Success(commentDao.deleteComment(postId, commentId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getComment(postId: String, commentId: String): Flow<Comment> {
        return userRepository.getUserData().flatMapLatest { user ->
            commentDao.getComment(postId, commentId).map {
                val earliest = it.earliestReply.map { reply ->
                    reply.asExternalModel(reply.likeUser.contains(user.uid))
                }
                val isLiked = it.likeUser.contains(user.uid)
                it.asExternalModel(earliest, isLiked)
            }
        }
    }

    override suspend fun addCommentLike(postId: String, commentId: String) {
        val user = userRepository.getUserData().first()

        commentDao.appendItemList(
            postId = postId,
            commentId = commentId,
            field = "like_user",
            items = user.uid
        )
    }

    override suspend fun removeCommentLike(postId: String, commentId: String) {
        val user = userRepository.getUserData().first()

        commentDao.removeItemList(
            postId = postId,
            commentId = commentId,
            field = "like_user",
            items = user.uid
        )
    }
}
