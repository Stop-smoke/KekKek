package com.agvber.kekkek.core.domain.model

import com.agvber.kekkek.core.domain.getElapsedDateTime

data class Notification(
    val id: String,
    val title: String,
    val body: String,
    val dateTime: DateTime,
    val category: NotificationCategory,
) {

    val modifiedElapsedDateTime = dateTime.modified.getElapsedDateTime()
}