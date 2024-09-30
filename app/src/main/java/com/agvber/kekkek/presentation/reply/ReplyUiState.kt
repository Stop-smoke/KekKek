package com.agvber.kekkek.presentation.reply

import com.agvber.kekkek.core.domain.model.Reply

sealed interface ReplyUiState {

    data class ReplyType(val data: Reply) : ReplyUiState

    data object ItemDeleted : ReplyUiState
}