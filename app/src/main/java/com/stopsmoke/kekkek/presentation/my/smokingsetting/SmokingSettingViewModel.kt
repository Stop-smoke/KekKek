package com.stopsmoke.kekkek.presentation.my.smokingsetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.usecase.UpdateUserSmokingSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SmokingSettingViewModel @Inject constructor(
    private val updateUserSmokingSettingsUseCase: UpdateUserSmokingSettingsUseCase,
) : ViewModel() {

    private val _perDay = MutableStateFlow("") // 하루에 담배 몇개비 피우나요?
    val perDay = _perDay.asStateFlow()

    fun updateSmokingPerDay(perDay: String) {
        viewModelScope.launch {
            _perDay.emit(perDay)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val perDayUiState: Flow<SmokingSettingUiState> = perDay.mapLatest {
        try {
            if (it == "") {
                return@mapLatest SmokingSettingUiState.Loading
            }

            val day = it.toInt()

            if (day == 0) {
                return@mapLatest SmokingSettingUiState.Loading
            }

            SmokingSettingUiState.Success
        } catch (e: Exception) {
            SmokingSettingUiState.Error(e)
        }
    }

    private val _perPack = MutableStateFlow("") // 담배 한 팩에 몇 개비가 들어있나요?
    val perPack = _perPack.asStateFlow()

    fun updateSmokingPerPack(perPack: String) {
        viewModelScope.launch {
            _perPack.emit(perPack)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val perPackUiState = perPack.mapLatest {
        try {
            if (it == "") {
                return@mapLatest SmokingSettingUiState.Loading
            }

            val pack = it.toInt()

            if (pack == 0) {
                return@mapLatest SmokingSettingUiState.Loading
            }

            SmokingSettingUiState.Success
        } catch (e: Exception) {
            SmokingSettingUiState.Error(e)
        }
    }

    private val _packPrice = MutableStateFlow("") // 담배 한 팩 당 가격이 얼마인가요?
    val packPrice = _packPrice.asStateFlow()

    fun updateSmokingPackPrice(price: String) {
        viewModelScope.launch {
            _packPrice.emit(price)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val packPriceUiState = packPrice.mapLatest {
        try {
            if (it == "") {
                return@mapLatest SmokingSettingUiState.Loading
            }

            val price = it.toInt()

            if (price == 0) {
                return@mapLatest SmokingSettingUiState.Loading
            }

            SmokingSettingUiState.Success
        } catch (e: Exception) {
            SmokingSettingUiState.Error(e)
        }
    }

    private val _resultUserSettings =
        MutableStateFlow<SmokingSettingUiState>(SmokingSettingUiState.Loading)
    val resultUserSettings = _resultUserSettings.asStateFlow()

    fun updateUserConfig() {
        viewModelScope.launch {
            try {
                updateUserSmokingSettingsUseCase(
                    perDay.value.toInt(),
                    perPack.value.toInt(),
                    packPrice.value.toInt(),
                )
                _resultUserSettings.emit(SmokingSettingUiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                _resultUserSettings.emit(SmokingSettingUiState.Error(e))
            }
        }
    }
}

sealed interface SmokingSettingUiState {

    data object Success : SmokingSettingUiState

    data object Loading : SmokingSettingUiState

    data class Error(val t: Throwable?) : SmokingSettingUiState

}