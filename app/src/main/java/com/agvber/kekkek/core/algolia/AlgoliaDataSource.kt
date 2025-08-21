package com.agvber.kekkek.core.algolia

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.IndexName
import com.algolia.search.model.search.Query
import com.agvber.kekkek.core.algolia.model.SearchPostEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlgoliaDataSource @Inject constructor(
    private val clientSearch: ClientSearch,
) {

    fun searchPost(query: String): Flow<PagingData<SearchPostEntity>> {
        val hitsSearcher = HitsSearcher(
            client = clientSearch,
            indexName = IndexName(POST_INDEX_NAME),
            query = Query(query)
        )

        return Paginator(
            searcher = hitsSearcher,
            pagingConfig = PagingConfig(pageSize = PAGE_SIZE),
            transformer = { it.deserialize(SearchPostEntity.serializer()) }
        )
            .flow
    }

    companion object {
        private const val POST_INDEX_NAME = "post"
        private const val PAGE_SIZE = 10
    }
}