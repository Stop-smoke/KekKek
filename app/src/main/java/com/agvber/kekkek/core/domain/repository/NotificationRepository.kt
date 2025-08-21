package com.agvber.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.agvber.kekkek.core.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getNotificationItems(): Result<Flow<PagingData<Notification>>>

    suspend fun deleteNotificationItem(id: String)
}