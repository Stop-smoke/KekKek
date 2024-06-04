package com.stopsmoke.kekkek.presentation.attainments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime

class AttainmentsViewModel : ViewModel() {

    // 온보딩에서 가져온 하루에 피는 개비 수, 일단 임시값 다 넣어놓음
    private val cigarettesPerDay = 8

    // 온보딩에서 가져온 한 갑당 담배 개비 수
    private val cigarettesPerPack = 20

    // 온보딩에서 입력받은 한 갑당 가격
    private val pricePerPack = 3530

    // 피우지 않은 담배 개비 수
    private val _cigarettesNotSmoked = MutableLiveData<Int>()
    val cigarettesNotSmoked: LiveData<Int> get() = _cigarettesNotSmoked

    // 절약한 총 금액
    private val _moneySaved = MutableLiveData<String>()
    val moneySaved: LiveData<String> get() = _moneySaved

    // 생명 연장 시간
    private val _lifeExtendedTime = MutableLiveData<String>()
    val lifeExtendedTime: LiveData<String> get() = _lifeExtendedTime

    // 경과한 날짜
    private val _elapsedDays = MutableLiveData<Int>()
    val elapsedDays: LiveData<Int> get() = _elapsedDays

    // 경과한 시간 (시:분:초)
    private val _elapsedTime = MutableLiveData<String>()
    val elapsedTime: LiveData<String> get() = _elapsedTime

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            val startTime = LocalDateTime.now().minusDays(3) // 예시로 3일 전 시작
            while (true) {
                updateElapsedTime(startTime)
                delay(1000) // 1초마다 업데이트
            }
        }
    }

    private suspend fun updateElapsedTime(startTime: LocalDateTime) {
        val currentDateTime = LocalDateTime.now()
        val period = Duration.between(startTime, currentDateTime)
        val days = period.toDays().toInt()
        val hours = period.toHours() % 24
        val minutes = period.toMinutes() % 60
        val seconds = period.seconds % 60

        _elapsedDays.postValue(days)
        _elapsedTime.postValue(String.format("%d:%02d:%02d", hours, minutes, seconds))

        updateAttainments(days)
    }

    private fun updateAttainments(days: Int) {
        // 경과한 시간에 따라 성과 계산 업데이트
        val cigarettesNotSmoked = cigarettesPerDay * days
        _cigarettesNotSmoked.postValue(cigarettesNotSmoked)

        val moneySaved = (cigarettesNotSmoked / cigarettesPerPack) * pricePerPack
        _moneySaved.postValue(moneySaved.toString())

        val lifeExtendedTime = calculateLifeExtendedTime(cigarettesNotSmoked)
        _lifeExtendedTime.postValue(lifeExtendedTime)
    }

    private fun calculateLifeExtendedTime(cigarettesNotSmoked: Int?): String {
        if (cigarettesNotSmoked == null) {
            return "0시간 0분"
        }
        val minutes = cigarettesNotSmoked * 5 // 1개비 당 5분씩 생명 연장
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return String.format("%d시간 %02d분", hours, remainingMinutes)
    }
}
