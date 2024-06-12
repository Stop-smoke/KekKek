package com.stopsmoke.kekkek.presentation.community

import android.os.Parcelable
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommunityWritingItem(
    val userInfo: UserInfo,
    val postInfo: PostInfo,
    val postImage: String,
    val post: String,
    val postTime: ElapsedDateTime
) : Parcelable

@Parcelize
data class CommunityPopularItem(
    val postInfo1: PostInfo,
    val postInfo2: PostInfo
) : Parcelable

@Parcelize
data class PostInfo(
    val id: String,
    val title: String,
    val postType: String,
    val view: Long,
    val like: Long,
    val comment: Long
) : Parcelable

@Parcelize
data class UserInfo(
    val uid: String,
    val name: String,
    val rank: Long,
    val profileImage: String
) : Parcelable


