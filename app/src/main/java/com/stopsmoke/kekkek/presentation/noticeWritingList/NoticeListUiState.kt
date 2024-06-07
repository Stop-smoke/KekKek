package com.stopsmoke.kekkek.presentation.noticeWritingList

data class NoticeListUiState(
    val list: List<NoticeListItem>
) {
    companion object {
        fun init() = NoticeListUiState(
            list = emptyList()
        )
    }
}
