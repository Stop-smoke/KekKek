package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.RecommendedKeyword
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun getRecommendedKeyword(): Flow<List<RecommendedKeyword>>

    fun searchPost(query: String): Flow<PagingData<Post>>
}