package com.stopsmoke.kekkek.presentation.my.smokingsetting

sealed interface SmokingSettingUiState {
    data object InitUiState: SmokingSettingUiState
    data class NormalUiState(val item: SmokingSettingItem): SmokingSettingUiState
    data object ErrorExit: SmokingSettingUiState
    data object ErrorMissing: SmokingSettingUiState
}