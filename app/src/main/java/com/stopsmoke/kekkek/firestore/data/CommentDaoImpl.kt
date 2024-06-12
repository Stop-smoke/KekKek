package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.stopsmoke.kekkek.firestore.dao.CommentDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class CommentDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : CommentDao {

    override fun getComment(postId: String): Flow<PagingData<CommentEntity>> {
        val query = firestore.collection(COLLECTION)
            .whereEqualTo("post_data.post_id", postId)
            .orderBy("date_time.created", Query.Direction.ASCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = CommentEntity::class.java
            )
        }
            .flow
    }

    override fun getMyCommentItems(uid: String): Flow<PagingData<CommentEntity>> {
        val query = firestore.collection(COLLECTION)
            .whereEqualTo("written.uid", uid)
            .orderBy("date_time", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = CommentEntity::class.java
            )
        }.flow
    }

    override fun getCommentItems(commentIdList: List<String>): Flow<PagingData<CommentEntity>> {
        try {
            val query = firestore.collection(COLLECTION)
                .whereEqualTo("id", commentIdList)
                .orderBy("date_time", Query.Direction.DESCENDING)

            return Pager(
                config = PagingConfig(PAGE_LIMIT)
            ) {
                FireStorePagingSource(
                    query = query,
                    limit = PAGE_LIMIT.toLong(),
                    clazz = CommentEntity::class.java
                )
            }.flow
        } catch (e: Exception) {
            e.printStackTrace()
            return Pager(
                config = PagingConfig(PAGE_LIMIT)
            ) {
                object : PagingSource<Int, CommentEntity>() {
                    override fun getRefreshKey(state: PagingState<Int, CommentEntity>): Int? = null

                    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentEntity> {
                        return LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null
                        )
                    }
                }
            }.flow
        }
    }


        override suspend fun addComment(commentEntity: CommentEntity) {
            firestore
                .collection(COLLECTION)
                .document().let { documentReference ->
                    documentReference.set(
                        commentEntity.copy(id = documentReference.id)
                    )
                }
                .addOnFailureListener { throw it }
                .addOnCanceledListener { throw CancellationException() }
                .await()
        }

        override suspend fun updateOrInsertComment(commentEntity: CommentEntity) {
            firestore
                .collection(COLLECTION)
                .document(commentEntity.id!!)
                .set(commentEntity)
                .addOnFailureListener { throw it }
                .addOnCanceledListener { throw CancellationException() }
                .await()
        }

        override suspend fun deleteComment(commentId: String) {
            firestore
                .collection(COLLECTION)
                .document(commentId)
                .delete()
                .addOnFailureListener { throw it }
                .addOnCanceledListener { throw CancellationException() }
                .await()
        }

        companion object {
            private const val COLLECTION = "comment"
            private const val PAGE_LIMIT = 30
        }
    }