package com.stopsmoke.kekkek.presentation.bookmark

import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import java.util.Date

data class BookmarkWritingItem(
    val userInfo: UserInfo,
    val postInfo : PostInfo,
    val postImage: String,
    val post: String,
    val postTime: Date
)

data class PostInfo(
    val title: String,
    val postType: String,
    val view: Int,
    val like: Int,
    val comment: Int
)

data class UserInfo(
    val name: String,
    val rank: Int,
    val profileImage: String
)