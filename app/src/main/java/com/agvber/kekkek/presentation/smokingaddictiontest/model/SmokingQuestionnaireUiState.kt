package com.agvber.kekkek.presentation.smokingaddictiontest.model

sealed interface SmokingQuestionnaireUiState {

    data object Low : SmokingQuestionnaireUiState

    data object Medium : SmokingQuestionnaireUiState

    data object High : SmokingQuestionnaireUiState

}