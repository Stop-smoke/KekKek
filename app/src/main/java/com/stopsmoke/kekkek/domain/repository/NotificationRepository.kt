package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotificationItems(): Result<Flow<PagingData<Notification>>>

    suspend fun deleteNotificationItem(id: String)
}