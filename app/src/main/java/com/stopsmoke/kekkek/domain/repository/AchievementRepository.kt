package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun getAchievementItems(category: DatabaseCategory = DatabaseCategory.ALL): Result<Flow<List<Achievement>>>

    suspend fun getAchievementCount(category: DatabaseCategory = DatabaseCategory.ALL): Long
}