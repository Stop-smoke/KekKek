package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getBookmarkItems(): Flow<PagingData<Post>>
}