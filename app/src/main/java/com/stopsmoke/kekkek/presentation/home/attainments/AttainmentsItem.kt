package com.stopsmoke.kekkek.presentation.home.attainments

import com.stopsmoke.kekkek.data.mapper.emptyHistory
import com.stopsmoke.kekkek.domain.model.History

data class AttainmentsItem(
    val history: History,
    val savedMoney: Double,
    val savedLife: Double,
    val savedCigarette: Double,
    val savedDate: Double,
    val timeString: String
) {
    companion object {
        fun init() = AttainmentsItem(
            history = emptyHistory(),
            savedMoney = 0.0,
            savedLife = 0.0,
            savedCigarette = 0.0,
            savedDate = 0.0,
            timeString = ""
        )
    }
}