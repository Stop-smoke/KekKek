package com.agvber.kekkek.presentation.post.detail.model

sealed interface PostDetailUiState {
    data object InitUiState: PostDetailUiState

    data object ErrorExit: PostDetailUiState
}