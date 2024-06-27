package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.NotificationEntity
import kotlinx.coroutines.flow.Flow

interface AchievementDao {
    fun getAchievementItems(category: String? = null): Flow<PagingData<AchievementEntity>>

    suspend fun addAchievementItem(achievementEntity: AchievementEntity)

    suspend fun getAchievementCount(category: String?):Long
}