package com.stopsmoke.kekkek.presentation.my.smokingsetting

import androidx.lifecycle.ViewModel


class SmokingSettingViewModel : ViewModel() {
    private var smokingSettingItem: SmokingSettingItem = SmokingSettingItem(0, 0, 0)


    fun setDailyCigarettesSmoked(dailyCigarettesSmoked: Int) {
        smokingSettingItem = smokingSettingItem.copy(dailyCigarettesSmoked = dailyCigarettesSmoked)
    }

    fun setPackCigaretteCount(packCigaretteCount: Int) {
        smokingSettingItem = smokingSettingItem.copy(packCigaretteCount = packCigaretteCount)
    }

    fun setPackPrice(packPrice: Int) {
        smokingSettingItem = smokingSettingItem.copy(packPrice = packPrice)
    }
}