package com.stopsmoke.kekkek.presentation.community

sealed interface CommunityUiState {
    data class CommunityNormalUiState(
        val writingList: List<CommunityWritingItem>,
        val popularItem: CommunityPopularItem
    ) : CommunityUiState

    companion object {
        fun init() = CommunityUiState.CommunityNormalUiState(
            writingList = emptyList(),
            popularItem = CommunityPopularItem(
                postInfo1 = PostInfo(
                    title = "",
                    postType = "",
                    view = 0,
                    like = 0,
                    comment = 0
                ),
                postInfo2 = PostInfo(
                    title = "",
                    postType = "",
                    view = 0,
                    like = 0,
                    comment = 0
                ),
            )
        )
    }
}

