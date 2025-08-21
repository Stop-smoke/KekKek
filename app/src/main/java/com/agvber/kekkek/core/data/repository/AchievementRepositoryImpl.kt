package com.agvber.kekkek.core.data.repository

import com.agvber.kekkek.common.Result
import com.agvber.kekkek.core.data.mapper.asExternalModel
import com.agvber.kekkek.core.domain.model.Achievement
import com.agvber.kekkek.core.domain.model.DatabaseCategory
import com.agvber.kekkek.core.domain.repository.AchievementRepository
import com.agvber.kekkek.core.firestore.dao.AchievementDao
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

    override suspend fun getAchievementListItem(achievementIdList: List<String>): Flow<List<Achievement>> {
        return achievementDao.getAchievementListItem(achievementIdList).map{
            it.map {
                it.asExternalModel()
            }
        }
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