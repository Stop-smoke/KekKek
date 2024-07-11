package com.stopsmoke.kekkek.presentation.my

import com.stopsmoke.kekkek.core.domain.model.User

sealed interface MyUiState {
    data class LoggedUiState(val user: User) : MyUiState

    data object ErrorExit: MyUiState
}