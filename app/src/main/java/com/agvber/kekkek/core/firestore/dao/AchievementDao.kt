package com.agvber.kekkek.core.firestore.dao

import com.agvber.kekkek.core.firestore.model.AchievementEntity
import kotlinx.coroutines.flow.Flow

interface AchievementDao {
    fun getAchievementItems(category: String? = null): Flow<List<AchievementEntity>>

    suspend fun addAchievementItem(achievementEntity: AchievementEntity)

    suspend fun getAchievementCount(category: String?):Long

    suspend fun getAchievementListItem(achievementIdList: List<String>): Flow<List<AchievementEntity>>
}