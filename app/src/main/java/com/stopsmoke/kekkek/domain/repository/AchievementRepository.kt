package com.stopsmoke.kekkek.domain.repository

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {

    fun getAchievementItems(): Result<Flow<List<Achievement>>>
}