package com.stopsmoke.kekkek.core.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.core.firestore.dao.NotificationDao
import com.stopsmoke.kekkek.core.firestore.model.NotificationEntity
import com.stopsmoke.kekkek.core.firestore.pager.FireStorePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

internal class NotificationDaoImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : NotificationDao {
    override fun getNotificationItems(uid: String): Flow<PagingData<NotificationEntity>> {
        val collection = fireStore.collection(COLLECTION)
        val query = collection
            .whereEqualTo("uid", uid)
            .orderBy("dateTime", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(pageSize = 30)
        ) {
            FireStorePagingSource(query, 30, NotificationEntity::class.java)
        }
            .flow
    }

    override suspend fun addNotificationItem(notificationEntity: NotificationEntity) {
        val collection = fireStore.collection(COLLECTION)
        collection.document().let { documentReference ->
            documentReference.set(
                notificationEntity.copy(id = documentReference.id)
            )
        }
            .addOnFailureListener { throw it }
            .addOnCanceledListener { throw CancellationException() }
            .await()
    }

    override suspend fun deleteNotificationItem(id: String) {
        val collection = fireStore.collection(COLLECTION)
        collection.document(id).delete().await()
    }

    companion object {
        private const val COLLECTION = "notification"
    }
}