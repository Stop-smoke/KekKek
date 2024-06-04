package com.stopsmoke.kekkek.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class HomeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.init())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val startTime: Date = Date()

    init{
        startTimer()
    }
    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                val currentTime = Date()
                val elapsedTimeMillis = currentTime.time - startTime.time
                _uiState.update { prev ->
                    prev.copy(
                        homeItem = prev.homeItem.copy(
                            timerString = formatElapsedTime(
                                elapsedTimeMillis
                            )
                        )
                    )
                }
                delay(60000) // 1 minute
            }
        }
    }

    private fun formatElapsedTime(elapsedTimeMillis: Long): String {
        val totalMinutes = elapsedTimeMillis / 60000
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60
        return "${hours}시간 ${minutes}분"
    }
}