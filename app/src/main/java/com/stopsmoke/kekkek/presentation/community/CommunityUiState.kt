package com.stopsmoke.kekkek.presentation.community

sealed interface CommunityUiState {
    data class CommunityNormalUiState(
        val popularItem: List<CommunityWritingItem>,
        val popularItemNonPeriod: List<CommunityWritingItem>,
        val popularPeriod: Boolean = true
    ) : CommunityUiState

    data object ErrorExit: CommunityUiState

    companion object {
        fun init() = CommunityNormalUiState(
            popularItem = emptyList(),
            popularItemNonPeriod = emptyList()
        )
    }
}

