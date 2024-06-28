package com.stopsmoke.kekkek.presentation.achievement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.model.Activities
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.domain.repository.RankingRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.getTotalDay
import com.stopsmoke.kekkek.presentation.home.rankingList.RankingListItem
import com.stopsmoke.kekkek.presentation.home.rankingList.toRankingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    val user = userRepository.getUserData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )


    val activities: StateFlow<Result<Activities>> =
        userRepository.getActivities().asResult().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = Result.Loading
        )


    private val _currentProgressItem = MutableStateFlow<CurrentProgress>(emptyCurrentProgress())
    val currentProgressItem: StateFlow<CurrentProgress> = _currentProgressItem.asStateFlow()


    val achievements = currentProgressItem.flatMapLatest { progress ->
        achievementRepository.getAchievementItems()
            .let {
                when (it) {
                    is Result.Error -> {
                        it.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData ->
                        pagingData.map {
                            it.getItem().copy(
                                currentProgress = when (it.category) {
                                    DatabaseCategory.COMMENT -> progress.comment.toInt()
                                    DatabaseCategory.POST -> progress.post.toInt()
                                    DatabaseCategory.USER -> progress.user.toInt()
                                    DatabaseCategory.ACHIEVEMENT -> progress.achievement.toInt()
                                    DatabaseCategory.RANK -> progress.rank.toInt()
                                    else -> {
                                        0
                                    }
                                }
                            )
                        }
                    }
                }
            }
    }

    suspend fun getAchievementCount(): Long {
        return achievementRepository.getAchievementCount()
    }

    private fun Achievement.getItem() = AchievementItem(
        id = id,
        name = name,
        description = description,
        image = image,
        category = category,
        maxProgress = maxProgress,
        requestCode = requestCode
    )

    suspend fun getCurrentProgress() {
        val userData = user.value
        when (activities.value) {
            is Result.Success -> {
                when (userData) {
                    is User.Registered -> {
                        val list = userRepository.getAllUserData().map{user->
                            (user as User.Registered).toRankingListItem()
                        }.filter { item->
                            item.startTime != null
                        }.sortedBy {item ->
                            item.startTime!!
                        }.map {
                            it.userID
                        }
                        val activities = (activities.value as Result.Success).data
                        val userRank = list.indexOf(userData.uid)+1

                        _currentProgressItem.value = CurrentProgress(
                            user = userData.getTotalDay(),
                            comment = activities.commentCount,
                            post = activities.postCount,
                            rank = userRank.toLong(),
                            achievement = userData.clearAchievementsList.size.toLong()
                        )
                    }

                    else -> _currentProgressItem.value = emptyCurrentProgress()
                }
            }
            else ->{
                // ui state 변경
            }
        }
    }


    fun getCurrentItem() = currentProgressItem.value


    fun upDateUserAchievementList(achievementIdList: List<String>) = viewModelScope.launch {
        val userData = user.value
        if (userData is User.Registered) {
            val updateList =
                (userData.clearAchievementsList.toSet() + achievementIdList.toSet()).toList()
            userRepository.setUserData(
                userData.copy(
                    clearAchievementsList = updateList
                )
            )
        }
    }

}