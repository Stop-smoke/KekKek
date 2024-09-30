package com.agvber.kekkek.core.data.mapper

import com.agvber.kekkek.core.domain.model.DateTime
import com.agvber.kekkek.core.domain.model.Notification
import com.agvber.kekkek.core.domain.model.NotificationCategory
import com.agvber.kekkek.core.firestore.model.NotificationEntity
import java.time.LocalDateTime

internal fun NotificationEntity.asExternalModel(): Notification =
    Notification(
        id = id ?: "",
        title = title ?: "",
        body = body ?: "",
        dateTime = dateTime?.asExternalModel() ?: DateTime(
            LocalDateTime.MIN,
            LocalDateTime.MIN
        ),
        category = when (category) {
            "information" -> NotificationCategory.INFORMATION
            "community" -> NotificationCategory.COMMUNITY
            else -> NotificationCategory.UNRECOGNIZABLE
        }
    )
