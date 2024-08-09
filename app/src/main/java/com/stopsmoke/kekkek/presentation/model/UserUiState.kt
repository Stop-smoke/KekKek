package com.stopsmoke.kekkek.presentation.model

import com.stopsmoke.kekkek.core.domain.model.User

interface UserUiState {

    object Guest : UserUiState

    object Loading : UserUiState

    data class Registered(val data: User) : UserUiState

    data class Error(val t: Throwable?) : UserUiState
}