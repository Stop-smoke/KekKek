package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.data.mapper.toEntity
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.firestore.dao.ReplyDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReplyRepositoryImpl @Inject constructor(
    private val replyDao: ReplyDao
): ReplyRepository {
    override suspend fun addReply(reply: Reply): Result<Unit> {
        return try {
            replyDao.addReply(reply.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReply(postId: String, commentId: String): Flow<PagingData<Reply>> {
        return replyDao.getReply(postId, commentId).map { pagingData ->
            pagingData.map {
                it.asExternalModel()
            }
        }
    }
}