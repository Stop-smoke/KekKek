package com.stopsmoke.kekkek.presentation.achievement

import com.stopsmoke.kekkek.domain.model.DatabaseCategory

data class AchievementItem (
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val category: DatabaseCategory,
    val maxProgress: Int,
    val requestCode: String
)

data class CurrentProgress(
    val time: Long,
    val comment: Long,
    val post: Long,
    val rank: Long,
    val achievement: Long
)