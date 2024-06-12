package com.stopsmoke.kekkek.presentation.home

data class HomeUiState(
    val homeItem: HomeItem
) {
    companion object {
        fun init() = HomeUiState(
            homeItem = HomeItem(
                timeString = "0분",
                savedMoney = 0.0,
                savedLife = 0.0,
                rank = 10000,
                addictionDegree = "테스트 필요"
            )
        )
    }
}