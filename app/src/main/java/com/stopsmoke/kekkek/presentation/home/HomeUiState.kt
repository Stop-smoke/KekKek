package com.stopsmoke.kekkek.presentation.home

import com.stopsmoke.kekkek.data.mapper.emptyHistory
import com.stopsmoke.kekkek.domain.model.History

data class HomeUiState(
    val homeItem: HomeItem,
    val startTimerSate: Boolean
) {
    companion object {
        fun init() = HomeUiState(
            homeItem = HomeItem(
                timeString = "0분",
                savedMoney = 0.0,
                savedLife = 0.0,
                rank = 10000,
                addictionDegree = "테스트 필요",
                history = emptyHistory()
            ),
            startTimerSate = false
        )
    }
}