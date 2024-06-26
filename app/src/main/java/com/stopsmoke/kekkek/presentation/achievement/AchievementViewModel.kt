package com.stopsmoke.kekkek.presentation.achievement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val userData = runBlocking {
        userRepository.getUserData().first()
    }

    private var currentProgressItem: CurrentProgress = getCurrentProgress()
    private val _category = MutableStateFlow(DatabaseCategory.COMMENT)
    val category get() = _category.asStateFlow()

    val achievements = category.flatMapLatest { postCategory ->
        achievementRepository.getAchievementItems(postCategory)
            .let {
                when (it) {
                    is Result.Error -> {
                        it.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData ->
                        pagingData.map {
                            it.getItem()
                        }
                    }
                }
            }
    }.cachedIn(viewModelScope)


    private fun Achievement.getItem() = AchievementItem(
        id = id,
        name = name,
        description = description,
        image = image,
        category = category,
        maxProgress = maxProgress,
        requestCode = requestCode
    )

    private fun getCurrentProgress(): CurrentProgress {
        return when (userData) {
            is User.Registered -> CurrentProgress(
                time = ChronoUnit.DAYS.between(
                    userData.startTime ?: LocalDateTime.now(),
                    LocalDateTime.now()
                ),
                comment = userData.commentMy.size.toLong(),
                post = userData.postMy.size.toLong(),
                rank = userData.ranking,
                achievement = userData.clearAchievementsList.size.toLong()
            )

            else -> CurrentProgress(
                time = 0,
                comment = 0,
                post = 0,
                rank = 0,
                achievement = 0
            )
        }
    }

    fun getCurrentItem() = currentProgressItem

    fun upDateUserAchievementList(achievementId: String) = viewModelScope.launch {
        if (userData is User.Registered) {
            if (achievementId !in userData.clearAchievementsList)
                userRepository.setUserData(userData.copy(
                    clearAchievementsList = userData.clearAchievementsList.toMutableList().apply {
                        add(achievementId)
                    }
                ))
        }
    }
}