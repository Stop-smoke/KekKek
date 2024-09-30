package com.agvber.kekkek.presentation.settings.profile

sealed interface EditNameState {
    data object Success : EditNameState
    data object Duplication : EditNameState
    data object Empty : EditNameState
    data object Input : EditNameState
}