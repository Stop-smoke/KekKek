package com.stopsmoke.kekkek.presentation.my.smokingsetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.UserConfig
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmokingSettingViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private var smokingSettingItem: SmokingSettingItem = SmokingSettingItem(0, 0, 0)

    private val userData = userRepository.getUserData()

    fun setDailyCigarettesSmoked(dailyCigarettesSmoked: Int) {
        smokingSettingItem = smokingSettingItem.copy(dailyCigarettesSmoked = dailyCigarettesSmoked)
    }

    fun setPackCigaretteCount(packCigaretteCount: Int) {
        smokingSettingItem = smokingSettingItem.copy(packCigaretteCount = packCigaretteCount)
    }

    fun setPackPrice(packPrice: Int) {
        smokingSettingItem = smokingSettingItem.copy(packPrice = packPrice)
    }

    fun updateUserConfig() = viewModelScope.launch {
        userData.collect { user ->
            when (user) {
                is User.Registered -> {
                    userRepository.setUserData(
                        user.copy(
                            userConfig = user.userConfig.copy(
                                dailyCigarettesSmoked = smokingSettingItem.dailyCigarettesSmoked,
                                packCigaretteCount = smokingSettingItem.packCigaretteCount,
                                packPrice = smokingSettingItem.packPrice
                            )
                        )
                    )
                }

                else -> {}
            }
        }
    }
}