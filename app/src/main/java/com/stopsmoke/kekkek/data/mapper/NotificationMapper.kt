package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.Notification
import com.stopsmoke.kekkek.domain.model.NotificationCategory
import com.stopsmoke.kekkek.firestore.model.NotificationEntity
import java.time.LocalDateTime

internal fun NotificationEntity.asExternalModel(): Notification = Notification(
    id = id ?: "",
    title = title ?: "",
    body = body ?: "",
    dateTime = dateTime?.asExternalModel() ?: DateTime(LocalDateTime.MIN, LocalDateTime.MIN),
    category = when (category) {
        "information" -> NotificationCategory.INFORMATION
        "community" -> NotificationCategory.COMMUNITY
        else -> NotificationCategory.UNRECOGNIZABLE
    }
)
