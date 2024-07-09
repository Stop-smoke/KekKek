package com.stopsmoke.kekkek.core.firestore.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.stopsmoke.kekkek.core.firestore.dao.SearchDao
import com.stopsmoke.kekkek.core.firestore.model.RecommendedKeywordEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

internal class SearchDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : SearchDao {
    override fun getRecommendedKeyword(): Flow<List<RecommendedKeywordEntity>> {

        return firestore.collection(RECOMMENDED_KEYWORD_COLLECTION)
            .dataObjects<RecommendedKeywordEntity>()
            .mapNotNull { it }
    }

    companion object {
        private const val RECOMMENDED_KEYWORD_COLLECTION = "recommended_keyword"
    }
}