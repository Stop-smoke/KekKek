package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.firestore.dao.PostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
) : PostRepository {
    override fun getPost(categories: List<String>): Result<Flow<PagingData<Post>>> {
        return kotlin.runCatching {
            postDao.getPost(categories = categories)
                .map { pagingData ->
                    pagingData.map {
                        it.asExternalModel()
                    }
                }
        }
    }
}