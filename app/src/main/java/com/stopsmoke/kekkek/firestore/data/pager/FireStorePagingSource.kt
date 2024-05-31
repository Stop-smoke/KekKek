package com.stopsmoke.kekkek.firestore.data.pager

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FireStorePagingSource<T : Any>(
    private val query: Query,
    private val limit: Long,
    private val clazz: Class<T>,
) : PagingSource<QuerySnapshot, T>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, T> =
        try {
            val currentPage = params.key ?: query.limit(limit).get().await()
            val lastVisible = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisible).limit(limit).get().await()

            Log.i("FireStorePagingSource", "current size: ${currentPage.size()}")
            LoadResult.Page(
                data = currentPage.toObjects(clazz),
                prevKey = null,
                nextKey = getNextPage(params, nextPage)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, T>): QuerySnapshot? {
        return null
    }

    private fun getNextPage(
        params: LoadParams<QuerySnapshot>,
        nextPage: QuerySnapshot,
    ): QuerySnapshot? {
        if (params.placeholdersEnabled && !nextPage.isEmpty) {
            return nextPage
        }
        return null
    }
}