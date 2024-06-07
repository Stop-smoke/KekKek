package com.stopsmoke.kekkek.presentation.myWritingList

class MyWritingListUiState(
    val list: List<MyWritingListItem>
) {
    companion object {
        fun init() = MyWritingListUiState(
            list = emptyList()
        )
    }
}
