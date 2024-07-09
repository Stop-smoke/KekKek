package com.stopsmoke.kekkek.presentation.post.popular

import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem

data class PopularPostUiState (
    val list: List<CommunityWritingItem>,
    val isLoading: Boolean =false
) {
    companion object {
        fun init() = PopularPostUiState(
            list = emptyList()
        )
    }
}
