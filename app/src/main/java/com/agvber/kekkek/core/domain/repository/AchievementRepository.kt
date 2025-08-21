package com.agvber.kekkek.core.domain.repository

import com.agvber.kekkek.common.Result
import com.agvber.kekkek.core.domain.model.Achievement
import com.agvber.kekkek.core.domain.model.DatabaseCategory
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun getAchievementItems(category: DatabaseCategory = DatabaseCategory.ALL): Result<Flow<List<Achievement>>>

    suspend fun getAchievementCount(category: DatabaseCategory = DatabaseCategory.ALL): Long

    suspend fun getAchievementListItem(achievementIdList: List<String>): Flow<List<Achievement>>
}