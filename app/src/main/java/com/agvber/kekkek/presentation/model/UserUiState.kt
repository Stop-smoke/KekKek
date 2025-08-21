package com.agvber.kekkek.presentation.model

import com.agvber.kekkek.core.domain.model.User

interface UserUiState {

    object Guest : UserUiState

    object Loading : UserUiState

    data class Registered(val data: User) : UserUiState

    data class Error(val t: Throwable?) : UserUiState
}