package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.firestore.dao.PostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
) : PostRepository {
    override fun getPost(category: PostCategory): Result<Flow<PagingData<Post>>> = try {
        val categoryString = when(category){
            PostCategory.NOTICE -> "notice"
            PostCategory.QUIT_SMOKING_SUPPORT -> "quit_smoking_support"
            PostCategory.POPULAR -> "popular"
            PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
            PostCategory.SUCCESS_STORIES -> "success_stories"
            PostCategory.GENERAL_DISCUSSION -> "general_discussion"
            PostCategory.FAILURE_STORIES -> "failure_stories"
            PostCategory.RESOLUTIONS -> "resolutions"
            PostCategory.UNKNOWN -> null
            PostCategory.ALL -> null
        }
        postDao.getPost(category = categoryString)
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }
            .let {
                Result.Success(it)
            }

    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(e)
    }
}