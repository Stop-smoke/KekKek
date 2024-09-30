package com.agvber.kekkek.core.firestore.dao

import com.agvber.kekkek.core.firestore.model.RecommendedKeywordEntity
import kotlinx.coroutines.flow.Flow

interface SearchDao {

    fun getRecommendedKeyword(): Flow<List<RecommendedKeywordEntity>>
}