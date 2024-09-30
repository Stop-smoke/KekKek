package com.agvber.kekkek.presentation.post.popular

import com.agvber.kekkek.presentation.community.CommunityWritingItem


sealed interface PopularPostUiState{
    data class NormalUiState(
        val popularPostList: List<CommunityWritingItem>,
        val period: Boolean = true
    ): PopularPostUiState

    data object ErrorExit: PopularPostUiState
    data object ErrorMissing: PopularPostUiState

    companion object {
        fun init() = PopularPostUiState.NormalUiState(
            popularPostList = emptyList()
        )
    }
}
