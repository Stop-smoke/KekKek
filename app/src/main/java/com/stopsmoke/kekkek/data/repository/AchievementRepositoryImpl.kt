package com.stopsmoke.kekkek.data.repository

import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor() : AchievementRepository {

    override fun getAchievementItems(): Result<Flow<List<Achievement>>> {
        return Result.Success(
            flowOf(
                listOf(
                    Achievement(
                        id = "asdweiuhcn32",
                        title = "돼지 저금통",
                        description = "50개비를 금연하세요.",
                        imageUrl = "",
                        maxProgress = 50
                    )
                )
            )
        )
    }
}