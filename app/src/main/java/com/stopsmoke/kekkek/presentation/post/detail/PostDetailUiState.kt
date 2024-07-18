package com.stopsmoke.kekkek.presentation.post.detail

sealed interface PostDetailUiState {
    data object Init: PostDetailUiState
    data object ErrorExit: PostDetailUiState
    data object ErrorMissing: PostDetailUiState


    companion object{
        fun init() = PostDetailUiState.Init
    }
}