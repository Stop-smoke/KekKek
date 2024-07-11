package com.stopsmoke.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.core.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getBookmarkItems(): Flow<PagingData<Post>>
}