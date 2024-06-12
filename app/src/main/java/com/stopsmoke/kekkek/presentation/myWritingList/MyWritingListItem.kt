package com.stopsmoke.kekkek.presentation.myWritingList

import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo

data class MyWritingListItem(
    val userInfo: UserInfo,
    val postInfo: PostInfo,
    val postImage: String,
    val post: String,
    val postTime: ElapsedDateTime,
    val postType: PostCategory
)
