package com.stopsmoke.kekkek.presentation.community

sealed interface CommunityUiState {
    data class CommunityNormalUiState(
        val popularItem: CommunityPopularItem,
        val isLoading: Boolean = false
    ) : CommunityUiState

    companion object {
        fun init() = CommunityUiState.CommunityNormalUiState(
            popularItem = CommunityPopularItem(
                postInfo1 = emptyCommunityWritingListItem(),
                postInfo2 = emptyCommunityWritingListItem()
            ),
        )
    }
}

