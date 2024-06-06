package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPost(categories: List<String> = emptyList()): Result<Flow<PagingData<Post>>>
}