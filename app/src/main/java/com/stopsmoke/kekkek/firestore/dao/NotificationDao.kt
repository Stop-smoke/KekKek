package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface NotificationDao {

    fun getNotificationItems(uid: String): Flow<PagingData<NotificationEntity>>

    suspend fun addNotificationItem(notificationEntity: NotificationEntity)

    suspend fun deleteNotificationItem(id: String)
}