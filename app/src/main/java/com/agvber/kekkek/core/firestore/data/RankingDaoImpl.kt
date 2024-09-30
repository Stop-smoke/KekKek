package com.agvber.kekkek.core.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.agvber.kekkek.core.domain.model.RankingCategory
import com.agvber.kekkek.core.firestore.USER_COLLECTION
import com.agvber.kekkek.core.firestore.dao.RankingDao
import com.agvber.kekkek.core.firestore.model.RankingEntity
import com.agvber.kekkek.core.firestore.pager.FireStorePagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RankingDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : RankingDao {
    override suspend fun getRankingList(
        uid: String,
        category: RankingCategory
    ): Flow<PagingData<RankingEntity>> {

        val query = firestore.collection(USER_COLLECTION)
            .orderBy("history.quit_smoking_start_date_time", Query.Direction.ASCENDING)
            .orderBy("history.quit_smoking_stop_date_time", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {

            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = RankingEntity::class.java
            )

        }
            .flow

    }


    companion object {
        private const val PAGE_LIMIT = 30
    }
}