package com.agvber.kekkek.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.agvber.kekkek.common.exception.GuestModeException
import com.agvber.kekkek.core.data.mapper.asExternalModel
import com.agvber.kekkek.core.domain.model.Notification
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.repository.NotificationRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import com.agvber.kekkek.core.firestore.dao.NotificationDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val notificationDao: NotificationDao,
) : NotificationRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getNotificationItems(): Result<Flow<PagingData<Notification>>> {
        return kotlin.runCatching {
            userRepository.getUserData().flatMapLatest { user ->
                if (user !is User.Registered) {
                    GuestModeException().printStackTrace()
                    return@flatMapLatest emptyFlow()
                }

                notificationDao.getNotificationItems(user.uid)
                    .map { pagingData ->
                        pagingData.map {
                            it.asExternalModel()
                        }
                    }
            }
        }
    }

    override suspend fun deleteNotificationItem(id: String) {
        notificationDao.deleteNotificationItem(id)
    }
}