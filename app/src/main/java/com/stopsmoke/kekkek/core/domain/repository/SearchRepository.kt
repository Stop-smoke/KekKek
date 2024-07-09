package com.stopsmoke.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.RecommendedKeyword
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecommendedKeyword(): Flow<List<RecommendedKeyword>>

    fun searchPost(query: String): Flow<PagingData<Post>>
}