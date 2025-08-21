package com.agvber.kekkek.presentation.my

import com.agvber.kekkek.presentation.model.UserUiState

sealed interface MyUiState {
    data class LoggedUiState(val user: UserUiState) : MyUiState

    data object ErrorExit: MyUiState
}