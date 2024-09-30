package com.agvber.kekkek.presentation.post.notice

import com.agvber.kekkek.core.domain.model.ElapsedDateTime
import com.agvber.kekkek.core.domain.model.PostCategory
import com.agvber.kekkek.presentation.community.PostInfo
import com.agvber.kekkek.presentation.community.UserInfo

data class NoticeListItem(
    val userInfo: UserInfo,
    val postInfo: PostInfo,
    val postImage: String,
    val post: String,
    val postTime: ElapsedDateTime,
    val postType: PostCategory
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