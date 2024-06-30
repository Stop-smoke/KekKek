package com.stopsmoke.kekkek.domain.model

data class Activities(
    val postCount: Long,
    val commentCount: Long,
    val bookmarkCount: Long,
)

fun emptyActivities() = Activities(0, 0, 0)