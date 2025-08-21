package com.agvber.kekkek.core.firestore.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.agvber.kekkek.core.firestore.RECOMMENDED_KEYWORD_COLLECTION
import com.agvber.kekkek.core.firestore.dao.SearchDao
import com.agvber.kekkek.core.firestore.model.RecommendedKeywordEntity
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

}