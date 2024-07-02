package com.stopsmoke.kekkek.presentation.community

import com.stopsmoke.kekkek.presentation.home.HomeUiState

sealed interface CommunityUiState {
    data class CommunityNormalUiState(
        val popularItem: CommunityPopularItem,
        val isLoading: Boolean = false
    ) : CommunityUiState

    data object ErrorExit: CommunityUiState

    companion object {
        fun init() = CommunityUiState.CommunityNormalUiState(
            popularItem = CommunityPopularItem(
                postInfo1 = emptyCommunityWritingListItem(),
                postInfo2 = emptyCommunityWritingListItem()
            ),
        )
    }
}

