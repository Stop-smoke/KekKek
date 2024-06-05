package com.stopsmoke.kekkek.firestore.dao

import com.stopsmoke.kekkek.firestore.model.RecommendedKeywordEntity
import kotlinx.coroutines.flow.Flow

interface SearchDao {

    fun getRecommendedKeyword(): Flow<List<RecommendedKeywordEntity>>
}