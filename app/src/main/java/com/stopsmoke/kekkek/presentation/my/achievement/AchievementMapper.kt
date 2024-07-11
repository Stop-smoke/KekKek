package com.stopsmoke.kekkek.presentation.my.achievement

import com.stopsmoke.kekkek.core.domain.model.Achievement


internal fun Achievement.getItem() = AchievementItem(
    id = id,
    name = name,
    description = description,
    image = image,
    category = category,
    maxProgress = maxProgress,
    requestCode = requestCode
)