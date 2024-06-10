package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.firestore.dao.AchievementDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.AchievementEntity
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AchievementDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
):AchievementDao {
    override fun getAchievementItems(category: String?): Flow<PagingData<AchievementEntity>> {
        val query = firestore.collection(AchievementDaoImpl.COLLECTION)
            .whereNotNullEqualTo("category", category)
            .orderBy("max", Query.Direction.DESCENDING)


        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = AchievementEntity::class.java
            )

        }.flow
    }

    companion object {
        private const val COLLECTION = "achievement"
        private const val PAGE_LIMIT = 30
    }

    private fun Query.whereNotNullEqualTo(field: String, value: Any?): Query {
        if (value == null) {
            return this
        }
        return whereEqualTo(field, value)
    }
}