package com.stopsmoke.kekkek.presentation.community

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CommunityWritingItem(
    val userInfo: UserInfo,
    val postInfo : PostInfo,
    val postImage: String,
    val post: String,
    val postTime: Date,
)
data class CommunityPopularItem(
    val postInfo1 : PostInfo,
    val postInfo2: PostInfo
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


