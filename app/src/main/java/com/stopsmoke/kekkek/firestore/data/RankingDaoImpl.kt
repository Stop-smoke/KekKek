package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.firestore.dao.RankingDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.RankingEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RankingDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : RankingDao {
    override suspend fun getRankingList(): Flow<PagingData<RankingEntity>> {

        val query = firestore.collection(COLLECTION)
            .orderBy("date_time", Query.Direction.ASCENDING)

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
        private const val COLLECTION = "ranking"
        private const val PAGE_LIMIT = 30
    }
}