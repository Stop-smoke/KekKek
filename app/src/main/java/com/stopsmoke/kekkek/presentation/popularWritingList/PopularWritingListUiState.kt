package com.stopsmoke.kekkek.presentation.popularWritingList

data class PopularWritingListUiState (
    val list: List<PopularWritingListItem>
) {
    companion object {
        fun init() = PopularWritingListUiState(
            list = emptyList()
        )
    }
}
