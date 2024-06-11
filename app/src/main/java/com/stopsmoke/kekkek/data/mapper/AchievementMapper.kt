package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.firestore.model.AchievementEntity

internal fun AchievementEntity.asExternalModel() = Achievement(
    id = id ?: "null",
    name = name ?: "null",
    content = content ?: "null",
    image = image ?: "null",
    category = when (category) {
        "comment" -> DatabaseCategory.COMMENT
        "post" -> DatabaseCategory.POST
        "user" -> DatabaseCategory.USER
        "achievement" -> DatabaseCategory.ACHIEVEMENT
        "rank" -> DatabaseCategory.RANK
        "all" -> DatabaseCategory.ALL
        else -> DatabaseCategory.ALL
    },
    maxProgress = maxProgress ?: 0,
)