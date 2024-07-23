package com.stopsmoke.kekkek.presentation.my

import com.stopsmoke.kekkek.presentation.model.UserUiState

sealed interface MyUiState {
    data class LoggedUiState(val user: UserUiState) : MyUiState

    data object ErrorExit: MyUiState
}