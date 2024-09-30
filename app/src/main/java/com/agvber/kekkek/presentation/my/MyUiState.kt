package com.agvber.kekkek.presentation.my

import com.agvber.kekkek.core.domain.model.User

sealed interface MyUiState {
    data class LoggedUiState(val user: User) : MyUiState

    data object ErrorExit: MyUiState
}