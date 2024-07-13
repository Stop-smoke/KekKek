package com.stopsmoke.kekkek.presentation.post.popular

import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem


sealed interface PopularPostUiState{
    data object Init: PopularPostUiState

    data object ErrorExit: PopularPostUiState
    data object ErrorMissing: PopularPostUiState

    companion object {
        fun init() = PopularPostUiState.Init
    }
}
