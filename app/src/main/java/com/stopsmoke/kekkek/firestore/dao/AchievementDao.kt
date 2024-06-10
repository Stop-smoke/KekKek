package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import kotlinx.coroutines.flow.Flow

interface AchievementDao {
    fun getAchievementItems(category: String? = null): Flow<PagingData<AchievementEntity>>
}