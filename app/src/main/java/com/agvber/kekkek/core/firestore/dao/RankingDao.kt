package com.agvber.kekkek.core.firestore.dao

import androidx.paging.PagingData
import com.agvber.kekkek.core.domain.model.RankingCategory
import com.agvber.kekkek.core.firestore.model.RankingEntity
import kotlinx.coroutines.flow.Flow

interface RankingDao {

    suspend fun getRankingList(
        uid: String,
        category: RankingCategory,
    ): Flow<PagingData<RankingEntity>>
}