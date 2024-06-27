package com.stopsmoke.kekkek.presentation.achievement

import com.stopsmoke.kekkek.domain.model.DatabaseCategory

data class AchievementItem (
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val category: DatabaseCategory,
    val maxProgress: Int,
    val currentProgress: Int = 0,
    val requestCode: String
)

data class CurrentProgress(
    val user: Long,
    val comment: Long,
    val post: Long,
    val rank: Long,
    val achievement: Long
)

fun emptyCurrentProgress() = CurrentProgress (
    user = 0,
    comment = 0,
    post = 0,
    rank = 10000,
    achievement = 0
)