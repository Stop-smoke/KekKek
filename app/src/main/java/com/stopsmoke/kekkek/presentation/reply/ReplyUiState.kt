package com.stopsmoke.kekkek.presentation.reply

import com.stopsmoke.kekkek.core.domain.model.Reply

sealed interface ReplyUiState {

    data class ReplyType(val data: Reply) : ReplyUiState

    data object ItemDeleted : ReplyUiState
}