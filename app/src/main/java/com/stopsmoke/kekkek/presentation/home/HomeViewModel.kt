package com.stopsmoke.kekkek.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.UserConfig
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.init())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val userData = userRepository.getUserData("테스트_계정")

    private var timeString: String = ""
    private var savedMoneyPerMinute: Double = 0.0
    private var savedLifePerMinute: Double = 0.0
    private var startTime = LocalDateTime.now()

    init {
        startTimer()
    }

    fun updateUserData(user: User) = viewModelScope.launch {
        startTime = (user as? User.Registered)?.startTime ?: LocalDateTime.now()
        val currentTime = LocalDateTime.now()
        val elapsedTimeMillis = Duration.between(startTime, currentTime).toMillis()
        val elapsedTimeMinutes = elapsedTimeMillis / (1000 * 60)

        when (user) {
            is User.Registered -> {
                timeString = formatElapsedTime(elapsedTimeMillis)
                calculateSavedValues(user.userConfig)
                _uiState.emit(
                    HomeUiState(
                        homeItem = HomeItem(
                            timeString = timeString,
                            savedMoney = savedMoneyPerMinute * elapsedTimeMinutes,
                            savedLife = savedLifePerMinute * elapsedTimeMinutes,
                            rank = user.ranking,
                            addictionDegree = "테스트 필요",
                        )
                    )
                )
            }

            else -> {}
        }
    }


    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                val currentTime = LocalDateTime.now()
                val elapsedTimeMillis = Duration.between(startTime, currentTime).toMillis()

                val tempTimeString = formatElapsedTime(elapsedTimeMillis)
                if (timeString != tempTimeString) {
                    timeString = tempTimeString
                    _uiState.update { prev ->
                        prev.copy(
                            homeItem = prev.homeItem.copy(
                                timeString = timeString,
                                savedLife = prev.homeItem.savedLife + savedLifePerMinute,
                                savedMoney = prev.homeItem.savedMoney + savedLifePerMinute
                            )
                        )
                    }
                }
                delay(1000) // 1 second
            }
        }
    }

    private fun formatElapsedTime(elapsedTimeMillis: Long): String {
        val totalMinutes = elapsedTimeMillis / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}시간 ${minutes}분"
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