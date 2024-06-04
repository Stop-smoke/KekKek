package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.domain.model.Notification
import com.stopsmoke.kekkek.domain.repository.NotificationRepository
import com.stopsmoke.kekkek.firestore.dao.NotificationDao
import com.stopsmoke.kekkek.storage.dao.UserDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val notificationDao: NotificationDao,
) : NotificationRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getNotificationItems(): Result<Flow<PagingData<Notification>>> {
        return kotlin.runCatching {
            userDao.getUserData().flatMapLatest { user ->
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