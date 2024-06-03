package com.stopsmoke.kekkek.presentation.community

sealed interface CommunityListItem {
    data class CommunityWritingItem(
        val userInfo: UserInfo,
        val postInfo : PostInfo,
        val postImage: String,
        val post: String
    ) : CommunityListItem

    data class CommunityPopularItem(
        val postCategory: String,
        val postInfo1 : PostInfo,
        val postInfo2: PostInfo
    ) : CommunityListItem

    data class CommunityCategoryItem(
        val categoryList: String
    ) : CommunityListItem
}

data class PostInfo(
    val title: String,
    val postType: String,
    val view: Int,
    val like: Int,
    val comment: Int
)

data class UserInfo(
    val name: String,
    val rank: String,
    val profileImage: String
)