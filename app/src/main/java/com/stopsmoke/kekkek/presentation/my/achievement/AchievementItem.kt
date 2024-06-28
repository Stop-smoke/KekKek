package com.stopsmoke.kekkek.presentation.my.achievement

import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import java.math.BigDecimal
import java.math.MathContext

data class AchievementItem(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val category: DatabaseCategory,
    val maxProgress: Int,
    val currentProgress: Int = 0,
    val requestCode: String
) {
    val progress: BigDecimal =when {
        currentProgress == 0 -> BigDecimal.ZERO
        category != DatabaseCategory.RANK -> {
            currentProgress.toBigDecimal()
                .divide(maxProgress.toBigDecimal(), MathContext.DECIMAL128)
        }

        else -> {
            maxProgress.toBigDecimal()
                .divide(currentProgress.toBigDecimal(), MathContext.DECIMAL128)
        }
    }
}

data class CurrentProgress(
    val user: Long,
    val comment: Long,
    val post: Long,
    val rank: Long,
    val achievement: Long
)

fun emptyCurrentProgress() = CurrentProgress(
    user = 0,
    comment = 0,
    post = 0,
    rank = 10000,
    achievement = 0
)