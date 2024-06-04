package com.stopsmoke.kekkek.presentation.home

import java.util.Calendar
import java.util.Date

data class HomeUiState(
    val homeItem: HomeItem
) {
    companion object {
        fun init() = HomeUiState(
            homeItem = HomeItem(
                timerString = "0분",
                savedMoney = 0,
                savedLife = 0,
                rank = 10000,
                noticeTitle = "공지 공지",
                addictionDegree = "테스트 필요"
            )
        )
    }
}