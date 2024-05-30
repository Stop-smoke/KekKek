package com.stopsmoke.kekkek.firestore.data.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import kotlinx.coroutines.tasks.await

class CommentPagingSource(
    private val query: Query,
) : PagingSource<QuerySnapshot, CommentEntity>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, CommentEntity>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, CommentEntity> =
        try {
            val currentPage = params.key ?: query.get().await()
            val lastVisible = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisible).get().await()

            LoadResult.Page(
                data = currentPage.toObjects(CommentEntity::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
}