package com.agvber.kekkek.core.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.agvber.kekkek.core.data.mapper.asExternalModel
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.repository.BookmarkRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import com.agvber.kekkek.core.firestore.dao.PostDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val userRepository: UserRepository,
) : BookmarkRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBookmarkItems(): Flow<PagingData<Post>> =
        userRepository.getUserData()
            .flatMapLatest { user ->
                postDao.getBookmarkItems((user as User.Registered).uid)
                    .map { pagingData ->
                        pagingData.map { it.asExternalModel() }
                    }

            }
}