package com.stopsmoke.kekkek.presentation.attainments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.UserConfig
import com.stopsmoke.kekkek.domain.model.getStartTimerState
import com.stopsmoke.kekkek.domain.model.getTotalMinutesTime
import com.stopsmoke.kekkek.domain.model.getTotalSecondsTime
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttainmentsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<AttainmentsItem> =
        MutableStateFlow(AttainmentsItem.init())
    val uiState: StateFlow<AttainmentsItem> = _uiState.asStateFlow()

    private var _currentUserState = MutableStateFlow<User>(User.Guest)
    val currentUserState = _currentUserState.asStateFlow()

    private var timeString: String = ""
    private var savedMoneyPerMinute: Double = 0.0
    private var savedLifePerMinute: Double = 0.0
    private var savedCigarettePerMinute: Double = 0.0
    private var savedDatePerMinute: Double = 0.0


    fun updateUserData() = viewModelScope.launch {
        val userData = userRepository.getUserData()
        userData.collect { user ->
            when (user) {
                is User.Registered -> {
                    _currentUserState.value = user

                    val totalMinutesTime = user.history.getTotalMinutesTime()
                    val totalSecondsTime = user.history.getTotalSecondsTime()
                    timeString = formatElapsedTime(totalMinutesTime)
                    calculateSavedValues(user.userConfig)
                    _uiState.emit(
                        AttainmentsItem(
                            history = user.history,
                            savedDate = savedDatePerMinute,
                            savedMoney = savedMoneyPerMinute * totalMinutesTime,
                            savedLife = savedLifePerMinute * totalMinutesTime,
                            savedCigarette = savedCigarettePerMinute * totalMinutesTime,
                            timeString = formatElapsedTime(totalSecondsTime)
                        )
                    )
                    if (user.history.getStartTimerState()) startTimer()
                }

                else -> {}
            }
        }
    }

    fun updateTestUserData() = viewModelScope.launch {
        val userData = userRepository.getUserData("테스트_계정")
        when (userData) {
            is Result.Success -> {
                userData.data.collect { user ->
                    _currentUserState.value = user

                    val totalMinutesTime = user.history.getTotalMinutesTime()
                    val totalSecondsTime = user.history.getTotalSecondsTime()
                    timeString = formatElapsedTime(totalMinutesTime)
                    calculateSavedValues(user.userConfig)
                    _uiState.emit(
                        AttainmentsItem(
                            history = user.history,
                            savedDate = savedDatePerMinute,
                            savedMoney = savedMoneyPerMinute * totalMinutesTime,
                            savedLife = savedLifePerMinute * totalMinutesTime,
                            savedCigarette = savedCigarettePerMinute * totalMinutesTime,
                            timeString = formatElapsedTime(totalSecondsTime)
                        )
                    )
                    if (user.history.getStartTimerState()) startTimer()
                }
            }

            else -> {}
        }
    }

    private fun startTimer() = viewModelScope.launch {
        var prevMinutes: Long = -1
        while (true) {
            timeString =
                formatElapsedTime(
                    (currentUserState.value as? User.Registered)?.history?.getTotalSecondsTime()
                        ?: 0
                )
            val currentMinutes =
                (currentUserState.value as? User.Registered)?.history?.getTotalMinutesTime() ?: 0

            _uiState.update { prev ->
                prev.copy(
                    timeString = timeString
                )
            }

            if (prevMinutes != currentMinutes) {
                _uiState.update { prev ->
                    prev.copy(
                        savedDate = savedDatePerMinute,
                        savedMoney = savedMoneyPerMinute * currentMinutes,
                        savedLife = savedLifePerMinute * currentMinutes,
                        savedCigarette = savedCigarettePerMinute * currentMinutes,
                    )
                }


                prevMinutes = currentMinutes
            }
            delay(1000) // 1 second
        }
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

        // 하루에 소비하는 담배 분당 생명 절약량
        savedLifePerMinute = totalCigarettesPerDay / totalMinutesSmokedPerDay

        // 하루에 소비하는 갑의 분당 비용
        savedMoneyPerMinute = totalPackCostPerDay / totalMinutesSmokedPerDay

        // 하루에 피는 담배의 분당 개수
        savedCigarettePerMinute = totalCigarettesPerDay / totalMinutesSmokedPerDay

        savedDatePerMinute = 0.0
    }


    private fun formatElapsedTime(elapsedTimeSeconds: Long): String {
        val days = elapsedTimeSeconds / (24 * 3600)
        val hours = (elapsedTimeSeconds % (24 * 3600)) / 3600
        val minutes = (elapsedTimeSeconds % 3600) / 60
        val seconds = elapsedTimeSeconds % 60

        val daysString = days.toString().padStart(2, '0')
        val hoursString = hours.toString().padStart(2, '0')
        val minutesString = minutes.toString().padStart(2, '0')
        val secondsString = seconds.toString().padStart(2, '0')

        return "${daysString}일 ${hoursString}:${minutesString}:${secondsString}"
    }

    fun timeStringToMinutes(timeString: String): Long {
        val parts = timeString.split("시간")
        val hours = if (parts.size > 1) parts[0].trim().toLong() else 0L
        val minutes = parts.last().replace("분", "").trim().toLong()
        return hours * 60 + minutes
    }
}
