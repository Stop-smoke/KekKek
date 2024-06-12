package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.RankingCategory
import com.stopsmoke.kekkek.firestore.model.RankingEntity
import kotlinx.coroutines.flow.Flow

interface RankingDao {

    suspend fun getRankingList(
        uid: String,
        category: RankingCategory,
    ): Flow<PagingData<RankingEntity>>
}