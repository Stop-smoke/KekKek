package com.stopsmoke.kekkek.domain.model

import com.stopsmoke.kekkek.domain.getElapsedDateTime

data class Notification(
    val id: String,
    val title: String,
    val body: String,
    val dateTime: DateTime,
    val category: NotificationCategory,
) {

    val modifiedElapsedDateTime = dateTime.modified.getElapsedDateTime()
}