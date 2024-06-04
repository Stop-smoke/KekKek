package com.stopsmoke.kekkek.domain.repository

import com.stopsmoke.kekkek.domain.model.RecommendedKeyword
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecommendedKeyword(): Result<Flow<List<RecommendedKeyword>>>
}