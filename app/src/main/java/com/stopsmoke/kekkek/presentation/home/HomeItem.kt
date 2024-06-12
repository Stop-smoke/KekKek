package com.stopsmoke.kekkek.presentation.home

import com.stopsmoke.kekkek.domain.model.History

data class HomeItem (
    val timeString: String,
    val savedMoney: Double,
    val savedLife: Double,
    val rank: Long,
    val addictionDegree: String,
    val history: History
)