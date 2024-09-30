package com.agvber.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.agvber.kekkek.core.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getBookmarkItems(): Flow<PagingData<Post>>
}