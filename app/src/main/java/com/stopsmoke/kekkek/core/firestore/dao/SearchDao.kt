package com.stopsmoke.kekkek.core.firestore.dao

import com.stopsmoke.kekkek.core.firestore.model.RecommendedKeywordEntity
import kotlinx.coroutines.flow.Flow

interface SearchDao {

    fun getRecommendedKeyword(): Flow<List<RecommendedKeywordEntity>>
}