package com.stopsmoke.kekkek.core.firestore.pager

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

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, T> {
        try {
            val currentPage = params.key ?: query.limit(limit).get().await()
            Log.i("FireStorePagingSource", "current size: ${currentPage.size()}")

            return LoadResult.Page(
                data = currentPage.toObjects(clazz),
                prevKey = getPrevKey(currentPage),
                nextKey = getNextPage(currentPage)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, T>): QuerySnapshot? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    private fun getPrevKey(current: QuerySnapshot): QuerySnapshot? {
        return null
    }

    private suspend fun getNextPage(current: QuerySnapshot): QuerySnapshot? {
        if (!current.isEmpty) {
            val lastVisible = current.documents.last()
            val nextPage = query
                .startAfter(lastVisible)
                .limit(limit)
                .get()
                .await()
                .also { if (it.isEmpty) return null }
            return nextPage
        }
        return null
    }
}