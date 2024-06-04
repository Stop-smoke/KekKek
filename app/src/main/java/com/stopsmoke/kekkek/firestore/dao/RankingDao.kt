package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.RankingEntity
import kotlinx.coroutines.flow.Flow

interface RankingDao {

    suspend fun getRankingList(): Flow<PagingData<RankingEntity>>
}