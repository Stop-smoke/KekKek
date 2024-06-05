package com.stopsmoke.kekkek.presentation.bookmark

data class BookmarkUiState (
    val bookmarkList: List<BookmarkWritingItem>
) {
    companion object{
        fun init() = BookmarkUiState(
            bookmarkList = emptyList()
        )
    }
}

