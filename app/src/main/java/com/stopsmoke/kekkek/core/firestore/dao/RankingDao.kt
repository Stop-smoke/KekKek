package com.stopsmoke.kekkek.core.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.core.domain.model.RankingCategory
import com.stopsmoke.kekkek.core.firestore.model.RankingEntity
import kotlinx.coroutines.flow.Flow

interface RankingDao {

    suspend fun getRankingList(
        uid: String,
        category: RankingCategory,
    ): Flow<PagingData<RankingEntity>>
}