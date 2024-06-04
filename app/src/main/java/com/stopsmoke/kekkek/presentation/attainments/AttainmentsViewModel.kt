package com.stopsmoke.kekkek.presentation.attainments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AttainmentsViewModel : ViewModel() {

    // 온보딩에서 가져온 하루에 피는 개비 수, 일단 임시값 다 넣어놓음
     private val cigarettesPerDay = 10

    // 온보딩에서 가져온 한 갑당 담배 개비 수
     private val cigarettesPerPack = 20

    // 온보딩에서 입력받은 한 갑당 가격
    private val pricePerPack = 3000

    // 피우지 않은 담배 개비 수
    private val _cigarettesNotSmoked = MutableLiveData<Int>()
    val cigarettesNotSmoked: LiveData<Int> get() = _cigarettesNotSmoked

    // 절약한 총 금액
    private val _moneySaved = MutableLiveData<String>()
    val moneySaved: LiveData<String> get() = _moneySaved

    init {
        calculateAchievements()
    }

    private fun calculateAchievements() {

        // 금연 시간 계산
        val antiSmokingTime = //딜레이를
        // 피우지 않은 담배 개비 수 계산

        // 절약한 총 금액 계산

    }

}