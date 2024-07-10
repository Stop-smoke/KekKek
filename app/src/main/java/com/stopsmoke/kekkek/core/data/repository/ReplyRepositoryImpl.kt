package com.stopsmoke.kekkek.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.exception.GuestModeException
import com.stopsmoke.kekkek.core.data.mapper.asExternalModel
import com.stopsmoke.kekkek.core.data.mapper.toEntity
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.firestore.dao.ReplyDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReplyRepositoryImpl @Inject constructor(
    private val replyDao: ReplyDao,
    private val userRepository: UserRepository
) : ReplyRepository {
    override suspend fun addReply(reply: Reply): Result<Unit> {
        return try {
            replyDao.addReply(reply.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReply(commentId: String): Flow<PagingData<Reply>> {
        val user = userRepository.getUserData().first() as? User.Registered

        return replyDao.getReply(commentId).map { pagingData ->
            pagingData.map {
                it.asExternalModel(isLiked = it.likeUser.contains(user?.uid))
            }
        }
    }

    override suspend fun deleteReply(reply: Reply) {
        replyDao.deleteReply(reply.toEntity())
    }

    override suspend fun updateReply(reply: Reply) {
        replyDao.updateReply(reply.toEntity())
    }

    override suspend fun appendReplyLike(postId: String, commentId: String, replyId: String) {
        val user = userRepository.getUserData().first() as? User.Registered
            ?: throw GuestModeException()
        replyDao.appendItemList(
            postId = postId,
            commentId = commentId,
            replyId = replyId,
            field = "like_user",
            items = listOf(user.uid)
        )
    }

    override suspend fun removeReplyLike(postId: String, commentId: String, replyId: String) {
        val user = userRepository.getUserData().first() as? User.Registered
            ?: throw GuestModeException()
        replyDao.removeItemList(
            postId = postId,
            commentId = commentId,
            replyId = replyId,
            field = "like_user",
            items = listOf(user.uid)
        )
    }
}