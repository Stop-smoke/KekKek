package com.stopsmoke.kekkek.firestore.dao

import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import kotlinx.coroutines.flow.Flow

interface AchievementDao {
    fun getAchievementItems(category: String? = null): Flow<List<AchievementEntity>>

    suspend fun addAchievementItem(achievementEntity: AchievementEntity)

    suspend fun getAchievementCount(category: String?):Long

    suspend fun getAchievementListItem(achievementIdList: List<String>): Flow<List<AchievementEntity>>
}