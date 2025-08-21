package com.agvber.kekkek.core.firestore.data

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.agvber.kekkek.core.firestore.ACHIEVEMENT_COLLECTION
import com.agvber.kekkek.core.firestore.dao.AchievementDao
import com.agvber.kekkek.core.firestore.model.AchievementEntity
import com.agvber.kekkek.core.firestore.whereNotNullEqualTo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class AchievementDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AchievementDao {
    override fun getAchievementItems(category: String?): Flow<List<AchievementEntity>> {
        return firestore.collection(ACHIEVEMENT_COLLECTION)
            .whereNotNullEqualTo("category", category)
            .orderBy("max_progress", Query.Direction.ASCENDING) //orderBy 지정 시 오류 발생..
            .dataObjects<AchievementEntity>()
            .mapNotNull { it }
    }

    override suspend fun addAchievementItem(achievementEntity: AchievementEntity) {
        val collection = firestore.collection(ACHIEVEMENT_COLLECTION)
        collection.document().let { documentReference ->
            documentReference.set(
                achievementEntity.copy(id = documentReference.id)
            )
        }
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun getAchievementCount(category: String?): Long {
        return try {
            firestore.collection(ACHIEVEMENT_COLLECTION)
                .whereNotNullEqualTo("category", category)
                .count()
                .get(AggregateSource.SERVER)
                .await()
                .count
        } catch (e: Exception) {
            throw e
        }

    }

    override suspend fun getAchievementListItem(achievementIdList: List<String>): Flow<List<AchievementEntity>> {
        if(achievementIdList.isEmpty()) return emptyFlow()
        return firestore.collection(ACHIEVEMENT_COLLECTION)
            .whereIn("id", achievementIdList)
            .dataObjects<AchievementEntity>()
    }


    companion object {
        private const val PAGE_LIMIT = 30
    }
}