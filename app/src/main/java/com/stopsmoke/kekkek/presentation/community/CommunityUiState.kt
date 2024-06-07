package com.stopsmoke.kekkek.presentation.community

import androidx.paging.PagingData

sealed interface CommunityUiState {
    data class CommunityNormalUiState(
        val popularItem: CommunityPopularItem
    ) : CommunityUiState

    companion object {
        fun init() = CommunityUiState.CommunityNormalUiState(
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

