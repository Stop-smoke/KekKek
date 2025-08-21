package com.agvber.kekkek.presentation.home

import com.agvber.kekkek.core.data.mapper.emptyHistory

sealed interface HomeUiState{
    data class NormalUiState(
        val homeItem: HomeItem,
        val startTimerSate: Boolean
    ):HomeUiState {
        companion object {
            fun init() = NormalUiState(
                homeItem = HomeItem(
                    timeString = "설정해주세요.",
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

    data object ErrorExit: HomeUiState
}