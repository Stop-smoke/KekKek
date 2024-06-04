package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Notification
import com.stopsmoke.kekkek.domain.model.NotificationCategory
import com.stopsmoke.kekkek.firestore.model.NotificationEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun NotificationEntity.asExternalModel(): Notification {

    val dateTime = kotlin.runCatching {
        LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME)
    }

    return Notification(
        id = id ?: "",
        title = title ?: "",
        body = body ?: "",
        dateTime = dateTime.getOrNull() ?: LocalDateTime.MIN,
        category = when (category) {
            "information" -> NotificationCategory.INFORMATION
            "community" -> NotificationCategory.COMMUNITY
            else -> NotificationCategory.UNRECOGNIZABLE
        }
    )
}