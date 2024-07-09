package com.stopsmoke.kekkek.presentation.home

import com.stopsmoke.kekkek.core.data.mapper.emptyHistory

sealed interface HomeUiState{
    data class NormalUiState(
        val homeItem: HomeItem,
        val startTimerSate: Boolean
    ):HomeUiState {
        companion object {
            fun init() = NormalUiState(
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

    data object ErrorExit: HomeUiState
}