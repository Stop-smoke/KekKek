package com.stopsmoke.kekkek.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.core.algolia.AlgoliaDataSource
import com.stopsmoke.kekkek.core.data.mapper.asExternalModel
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.RecommendedKeyword
import com.stopsmoke.kekkek.core.domain.repository.SearchRepository
import com.stopsmoke.kekkek.core.firestore.dao.SearchDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val searchDao: SearchDao,
    private val algoliaDataSource: AlgoliaDataSource,
) : SearchRepository {
    override fun getRecommendedKeyword(): Flow<List<RecommendedKeyword>> {
        return searchDao.getRecommendedKeyword()
            .map { keywords ->
                keywords.map { it.asExternalModel() }
            }
    }

    override fun searchPost(query: String): Flow<PagingData<Post>> {
        return algoliaDataSource.searchPost(query)
            .map { pagingData ->
                pagingData.map { it.asExternalModel() }
            }
    }
}