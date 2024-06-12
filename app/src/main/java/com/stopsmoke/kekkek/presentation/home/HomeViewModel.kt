package com.stopsmoke.kekkek.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.HistoryTime
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.UserConfig
import com.stopsmoke.kekkek.domain.model.getStartTimerState
import com.stopsmoke.kekkek.domain.model.getTotalMinutesTime
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.init())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var timeString: String = ""
    private var savedMoneyPerMinute: Double = 0.0
    private var savedLifePerMinute: Double = 0.0

    private var _currentUserState = MutableStateFlow<User>(User.Guest)
    val currentUserState = _currentUserState.asStateFlow()


    fun updateUserData() = viewModelScope.launch {
        val userData = userRepository.getUserData("테스트_계정")
        when (userData) {
            is Result.Success -> {
                userData.data.collect { user ->
                    _currentUserState.value = user

                    if (user.history.historyTimeList.isEmpty()) {
                        setEmptyStartUserHistory()
                    }

                    val totalMinutesTime = user.history.getTotalMinutesTime()
                    timeString = formatElapsedTime(totalMinutesTime)
                    calculateSavedValues(user.userConfig)
                    _uiState.emit(
                        HomeUiState(
                            homeItem = HomeItem(
                                timeString = timeString,
                                savedMoney = savedMoneyPerMinute * totalMinutesTime,
                                savedLife = savedLifePerMinute * totalMinutesTime,
                                rank = user.ranking,
                                addictionDegree = "테스트 필요",
                                history = user.history
                            ),
                            startTimerSate = user.history.getStartTimerState()
                        )
                    )
                }
            }

            else -> {}
        }
    }


    fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                timeString =
                    formatElapsedTime(
                        (currentUserState.value as? User.Registered)?.history?.getTotalMinutesTime()
                            ?: 0
                    )
                _uiState.update { prev ->
                    prev.copy(
                        homeItem = prev.homeItem.copy(
                            timeString = timeString
                        )
                    )
                }
                delay(1000) // 1 second
            }
        }
    }

    fun stopTimer() = viewModelScope.launch {
        timerJob?.cancel()
        timerJob = null

    }

    fun setStopUserHistory() = viewModelScope.launch {
        if (currentUserState.value is User.Registered) {
            val user = (currentUserState.value as User.Registered)
            val updatedHistoryTimeList = user.history.historyTimeList.toMutableList()
            val lastItem = updatedHistoryTimeList.last().copy(
                quitSmokingStopDateTime = LocalDateTime.now()
            )

            updatedHistoryTimeList[updatedHistoryTimeList.size - 1] = lastItem

            val updatedUserHistory =
                user.history.copy(
                    historyTimeList = updatedHistoryTimeList,
                    totalMinutesTime = timeStringToMinutes(uiState.value.homeItem.timeString)
                )
            userRepository.setUserData(user.copy(history = updatedUserHistory))

            updateUserData()
        }
    }

    fun setStartUserHistory() = viewModelScope.launch {
        if (currentUserState.value is User.Registered) {
            val user = currentUserState.value as User.Registered

            var updatedHistoryTimeList: MutableList<HistoryTime>? = null

            if (user.history.historyTimeList.isNotEmpty()) {
                updatedHistoryTimeList = user.history.historyTimeList.toMutableList()
            } else updatedHistoryTimeList = mutableListOf()

            val lastItem = HistoryTime(
                quitSmokingStartDateTime = LocalDateTime.now(),
                quitSmokingStopDateTime = null
            )
            updatedHistoryTimeList.add(lastItem)

            val updatedUserHistory =
                user.history.copy(historyTimeList = updatedHistoryTimeList)

            userRepository.setUserData(user.copy(history = updatedUserHistory))

            updateUserData()
        }
    }


    private fun setEmptyStartUserHistory() = viewModelScope.launch {
        if (currentUserState.value is User.Registered) {
            val user = currentUserState.value as User.Registered

            var updatedHistoryTimeList: MutableList<HistoryTime> = mutableListOf()
            val lastItem = HistoryTime(
                quitSmokingStartDateTime = LocalDateTime.now(),
                quitSmokingStopDateTime = null
            )
            updatedHistoryTimeList.add(lastItem)

            val updatedUserHistory =
                user.history.copy(historyTimeList = updatedHistoryTimeList)

            userRepository.setUserData(user.copy(history = updatedUserHistory))
        }
    }

    private fun formatElapsedTime(elapsedTimeMinutes: Long): String {
        val hours = elapsedTimeMinutes / 60
        val minutes = elapsedTimeMinutes % 60
        return if (hours > 0) "${hours}시간 ${minutes}분" else "${minutes}분"
    }

    fun timeStringToMinutes(timeString: String): Long {
        val parts = timeString.split("시간")
        val hours = if (parts.size > 1) parts[0].trim().toLong() else 0L
        val minutes = parts.last().replace("분", "").trim().toLong()
        return hours * 60 + minutes
    }

    private fun calculateSavedValues(userConfig: UserConfig) {
        // 하루에 피는 담배의 총 개수
        val totalCigarettesPerDay = userConfig.dailyCigarettesSmoked.toDouble()

        // 하루에 소비하는 갑의 개수
        val packsPerDay = totalCigarettesPerDay / userConfig.packCigaretteCount.toDouble()

        // 하루에 소비하는 갑의 비용
        val totalPackCostPerDay = packsPerDay * userConfig.packPrice.toDouble()

        // 하루에 소비하는 총 시간 (분 단위)
        val totalMinutesSmokedPerDay = 24 * 60.0 // 하루 전체 시간

        // 하루에 소비하는 담배 시간당 생명 절약량
        savedLifePerMinute = totalCigarettesPerDay / totalMinutesSmokedPerDay

        // 하루에 소비하는 갑의 시간당 비용
        savedMoneyPerMinute = totalPackCostPerDay / totalMinutesSmokedPerDay
    }

}