package com.stopsmoke.kekkek.data.repository

import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.domain.model.RecommendedKeyword
import com.stopsmoke.kekkek.domain.repository.SearchRepository
import com.stopsmoke.kekkek.firestore.dao.SearchDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val searchDao: SearchDao,
) : SearchRepository {
    override fun getRecommendedKeyword(): Result<Flow<List<RecommendedKeyword>>> {
        return kotlin.runCatching {
            searchDao.getRecommendedKeyword()
                .map { keywords ->
                    keywords.map { it.asExternalModel() }
                }
        }
    }
}