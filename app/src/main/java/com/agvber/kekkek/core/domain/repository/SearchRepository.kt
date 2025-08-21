package com.agvber.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.RecommendedKeyword
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecommendedKeyword(): Flow<List<RecommendedKeyword>>

    fun searchPost(query: String): Flow<PagingData<Post>>
}