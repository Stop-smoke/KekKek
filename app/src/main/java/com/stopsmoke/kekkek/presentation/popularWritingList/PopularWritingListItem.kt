package com.stopsmoke.kekkek.presentation.popularWritingList

import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import java.util.Date

class PopularWritingListItem (
    val userInfo: UserInfo,
    val postInfo : PostInfo,
    val postImage: String,
    val post: String,
    val postTime: ElapsedDateTime,
    val postType: PostCategory
){
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
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
    val rank: Int,
    val profileImage: String
)