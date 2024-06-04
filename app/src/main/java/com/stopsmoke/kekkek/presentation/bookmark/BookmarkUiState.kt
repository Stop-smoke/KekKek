package com.stopsmoke.kekkek.presentation.bookmark

import com.stopsmoke.kekkek.presentation.community.CommunityListItem
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import java.util.Date

data class BookmarkUiState (
    val bookmarkList: List<BookmarkWritingItem>
) {
    companion object{
        fun init() = BookmarkUiState(
            bookmarkList = emptyList()
        )
    }
}

