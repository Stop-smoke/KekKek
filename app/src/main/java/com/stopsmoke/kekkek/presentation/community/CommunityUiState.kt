package com.stopsmoke.kekkek.presentation.community

data class CommunityUiState (
    val communityCategory: CommunityCategory
) {
    companion object{
        fun init() = CommunityUiState(
            communityCategory = CommunityCategory.CommunityHome(
                popularItemList = emptyList(),
                noticeList = emptyList()
            )
        )
    }
}

sealed class CommunityCategory{
    data class CommunityHome(
        val popularItemList: List<CommunityListItem>,
        val noticeList: List<List<String>>
    ): CommunityCategory()

    data class CommunityList(
        val list: List<String>
    ): CommunityCategory()
}