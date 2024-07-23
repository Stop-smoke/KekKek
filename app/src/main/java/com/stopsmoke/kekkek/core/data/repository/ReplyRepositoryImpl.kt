package com.stopsmoke.kekkek.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.core.data.mapper.asExternalModel
import com.stopsmoke.kekkek.core.data.mapper.toEntity
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.firestore.dao.ReplyDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReplyRepositoryImpl @Inject constructor(
    private val replyDao: ReplyDao,
    private val userRepository: UserRepository,
) : ReplyRepository {

    override suspend fun addReply(reply: Reply): String {
        return replyDao.addReply(reply.toEntity())
    }

    override fun getReply(postId: String, commentId: String, replyId: String): Flow<Reply> =
        combine(
            userRepository.getUserData(),
            replyDao.getReply(postId, commentId, replyId)
        ) { user, reply ->
            reply.asExternalModel(
                isLiked = reply.likeUser.contains(user.uid)
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getReply(commentId: String): Flow<PagingData<Reply>> {
        return userRepository.getUserData().flatMapLatest { user ->
            replyDao.getReply(commentId).map { pagingData ->
                pagingData.map {
                    it.asExternalModel(isLiked = it.likeUser.contains(user.uid))
                }
            }
        }
    }

    override suspend fun deleteReply(postId: String, commentId: String, replyId: String) {
        replyDao.deleteReply(postId = postId, commentId = commentId, replyId = replyId)
    }

    override suspend fun updateReply(reply: Reply) {
        replyDao.updateReply(reply.toEntity())
    }

    override suspend fun appendReplyLike(postId: String, commentId: String, replyId: String) {
        val user = userRepository.getUserData().first()
        replyDao.appendItemList(
            postId = postId,
            commentId = commentId,
            replyId = replyId,
            field = "like_user",
            items = listOf(user.uid)
        )
    }

    override suspend fun removeReplyLike(postId: String, commentId: String, replyId: String) {
        val user = userRepository.getUserData().first()
        replyDao.removeItemList(
            postId = postId,
            commentId = commentId,
            replyId = replyId,
            field = "like_user",
            items = listOf(user.uid)
        )
    }
}