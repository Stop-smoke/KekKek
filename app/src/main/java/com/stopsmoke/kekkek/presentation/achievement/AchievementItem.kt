package com.stopsmoke.kekkek.presentation.achievement

import com.stopsmoke.kekkek.domain.model.DatabaseCategory

data class AchievementItem (
    val id: String,
    val name: String,
    val content: String,
    val image: String,
    val category: DatabaseCategory,
    val maxProgress: Int
)

data class CurrentProgress(
    val time: Int,
    val comment: Int,
    val post: Int,
    val rank: Int,
    val achievement: Int
)