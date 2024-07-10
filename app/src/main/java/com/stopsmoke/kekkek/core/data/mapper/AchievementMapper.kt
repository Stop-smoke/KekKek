package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.domain.model.Achievement
import com.stopsmoke.kekkek.core.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.core.firestore.model.AchievementEntity

fun AchievementEntity.asExternalModel() = Achievement(
    id = id ?: "null",
    name = name ?: "null",
    description = description ?: "null",
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
    requestCode = requestCode ?: "null"
)

fun Achievement.toEntity() = AchievementEntity(
    id = id,
    name = name,
    description = description,
    image = image,
    category = when (category) {
        DatabaseCategory.COMMENT ->"comment"
        DatabaseCategory.POST -> "post"
        DatabaseCategory.USER -> "user"
        DatabaseCategory.ACHIEVEMENT -> "achievement"
        DatabaseCategory.RANK -> "rank"
        DatabaseCategory.ALL -> "exception"
        else -> "exception"
    },
    maxProgress = maxProgress,
    requestCode = requestCode
)