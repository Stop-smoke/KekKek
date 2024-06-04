package com.stopsmoke.kekkek.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.domain.model.Notification
import com.stopsmoke.kekkek.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    notificationRepository: NotificationRepository,
) : ViewModel() {

    val notificationItem: Flow<PagingData<Notification>>? =
        notificationRepository.getNotificationItems()
            .getOrNull()
            ?.cachedIn(viewModelScope)
}