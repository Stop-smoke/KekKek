package com.stopsmoke.kekkek.presentation.my.smokingsetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.domain.usecase.UpdateUserSmokingSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmokingSettingViewModel @Inject constructor(
    private val updateUserSmokingSettingsUseCase: UpdateUserSmokingSettingsUseCase,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<SmokingSettingUiState> = MutableStateFlow(SmokingSettingUiState.InitUiState)
    val uiState: StateFlow<SmokingSettingUiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUserData().collect { user ->
                when (user) {
                    is User.Registered -> {
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
                    else -> {
                        _uiState.emit(
                            SmokingSettingUiState.ErrorMissing
                        )
                    }
                }
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
                _uiState.emit(
                    SmokingSettingUiState.ErrorExit
                )
            }
        }
    }
}