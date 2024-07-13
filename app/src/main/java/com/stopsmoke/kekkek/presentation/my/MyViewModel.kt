package com.stopsmoke.kekkek.presentation.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.model.Achievement
import com.stopsmoke.kekkek.core.domain.model.Activities
import com.stopsmoke.kekkek.core.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.model.emptyActivities
import com.stopsmoke.kekkek.core.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.getTotalDay
import com.stopsmoke.kekkek.presentation.my.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.my.achievement.CurrentProgress
import com.stopsmoke.kekkek.presentation.my.achievement.emptyCurrentProgress
import com.stopsmoke.kekkek.presentation.ranking.toRankingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MyUiState> = MutableStateFlow(MyUiState.LoggedUiState(User.Guest))
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    val user = userRepository.getUserData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )


    val activities: StateFlow<Result<Activities>> =
        userRepository.getActivities()
            .asResult()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = Result.Loading
            )


    private val _currentProgressItem = MutableStateFlow<CurrentProgress>(emptyCurrentProgress())
    val currentProgressItem: StateFlow<CurrentProgress> = _currentProgressItem.asStateFlow()


    val achievements: Flow<List<AchievementItem>> = currentProgressItem.flatMapLatest { progress ->
        try {
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
                            }.sortedAchievement()
                        }
                    }
                }
        } catch (e: Exception) {
            _uiState.emit(MyUiState.ErrorExit)
            emptyFlow()
        }
    }

    private fun List<AchievementItem>.sortedAchievement(): List<AchievementItem>{
        val clearList = this.filter { it.progress >= 1.0.toBigDecimal() }
        val nonClearList = this.filter { it !in clearList }.sortedByDescending { it.progress }

        val insertClearList = clearList.filter { it.id !in (user.value as User.Registered).clearAchievementsList }
        if(insertClearList.isNotEmpty()) {
            upDateUserAchievementList(insertClearList.map { it.id })
        }
        return nonClearList + clearList
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
                        val list = userRepository.getAllUserData().map { user ->
                            (user as User.Registered).toRankingListItem()
                        }.filter { item ->
                            item.startTime != null
                        }.sortedBy { item ->
                            item.startTime!!
                        }.map {
                            it.userID
                        }
                        val activities =
                            (activities.value as? Result.Success)?.data ?: emptyActivities()
                        val userRank = list.indexOf(userData.uid) + 1

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

            else -> {
                _uiState.emit(MyUiState.ErrorExit)
            }
        }
    }


    fun getCurrentItem() = currentProgressItem.value


    fun upDateUserAchievementList(achievementIdList: List<String>) = viewModelScope.launch {
        try {
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
        } catch (e: Exception) {
            _uiState.emit(MyUiState.ErrorExit)
        }
    }

    private val _currentClearAchievementIdList = MutableStateFlow<List<String>>(emptyList())
    val currentClearAchievementIdList = _currentClearAchievementIdList.asStateFlow()

    val currentClearAchievementList =
        currentClearAchievementIdList.flatMapLatest { achievementIdList ->
            achievementRepository.getAchievementListItem(achievementIdList).asResult()
        }

    fun setAchievementIdList(list: List<String>) {
        _currentClearAchievementIdList.value = list
    }
}
