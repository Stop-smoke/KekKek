package com.stopsmoke.kekkek.presentation.community

data class CommunityUiState (
    val communityListItem: List<CommunityListItem>
) {
    companion object{
        fun init() = CommunityUiState(
            communityListItem = emptyList()
        )
    }
}
