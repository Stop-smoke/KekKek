package com.stopsmoke.kekkek.presentation.my.smokingsetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmokingSettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SmokingSettingUiState> =
        MutableStateFlow(SmokingSettingUiState.InitUiState)
    val uiState: StateFlow<SmokingSettingUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                userRepository.getUserData().collect { user ->
                    val userConfig = user.userConfig
                    _uiState.emit(
                        SmokingSettingUiState.NormalUiState(
                            SmokingSettingItem(
                                dailyCigarettesSmoked = userConfig.dailyCigarettesSmoked,
                                packCigaretteCount = userConfig.packCigaretteCount,
                                packPrice = userConfig.packPrice
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.emit(SmokingSettingUiState.ErrorMissing)
            }
        }
    }

    val user = userRepository.getUserData()


    fun updateUserConfig(smokingSettingType: SmokingSettingType, newValue: Int) {
        viewModelScope.launch {
            try {
                userRepository.updateUserData(
                    mapOf(
                        smokingSettingType.toFieldString() to newValue
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.emit(
                    SmokingSettingUiState.ErrorExit
                )
            }
        }
    }
}