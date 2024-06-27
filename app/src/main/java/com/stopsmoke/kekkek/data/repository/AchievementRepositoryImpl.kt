package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.firestore.dao.AchievementDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val achievementDao: AchievementDao
) : AchievementRepository {
    override fun getAchievementItems(category: DatabaseCategory): Result<Flow<List<Achievement>>> =
        try {
            val categoryString = category.toRequestString()

            achievementDao.getAchievementItems(category = categoryString).map { data ->
                data.map {
                    it.asExternalModel()
                }
            }.let {
                    Result.Success(it)
                }

        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

    override suspend fun getAchievementCount(category: DatabaseCategory): Long {
        return achievementDao.getAchievementCount(category.toRequestString())
    }



    private fun DatabaseCategory.toRequestString():String? = when(this){
        DatabaseCategory.COMMENT -> "comment"
        DatabaseCategory.POST -> "post"
        DatabaseCategory.USER -> "user"
        DatabaseCategory.ACHIEVEMENT -> "achievement"
        DatabaseCategory.RANK -> "rank"
        DatabaseCategory.ALL -> null
    }
}