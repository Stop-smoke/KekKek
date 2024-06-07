package com.stopsmoke.kekkek.firestore.model

data class NotificationEntity(
    val id: String? = null,
    val uid: String? = null,
    val title: String? = null,
    val body: String? = null,
    val dateTime: DateTimeEntity? = null,
    val category: String? = null,
)