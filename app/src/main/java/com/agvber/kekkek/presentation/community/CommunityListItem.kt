package com.agvber.kekkek.presentation.community

import android.os.Parcelable
import com.agvber.kekkek.core.domain.model.DateTimeUnit
import com.agvber.kekkek.core.domain.model.ElapsedDateTime
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.PostCategory
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.presentation.mapper.toStringKR
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommunityWritingItem(
    val userInfo: UserInfo,
    val postInfo: PostInfo,
    val postImage: String,
    val post: String,
    var bookmark: Boolean = false,
    val postTime: ElapsedDateTime,
    val postType: PostCategory
) : Parcelable

@Parcelize
data class CommunityPopularItem(
    val postInfo1: CommunityWritingItem,
    val postInfo2: CommunityWritingItem
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


fun emptyCommunityWritingListItem() = CommunityWritingItem(
    userInfo = UserInfo(
        uid = "",
        name = "",
        rank = 0,
        profileImage = ""
    ),
    postInfo = PostInfo(
        id = "",
        title = "",
        postType = "",
        view = 0,
        like = 0,
        comment = 0
    ),
    postImage = "",
    post = "",
    postTime = ElapsedDateTime(
        DateTimeUnit.DAY, 0
    ),
    postType = PostCategory.UNKNOWN
)


fun Post.toCommunityWritingListItem() = CommunityWritingItem(
    userInfo = UserInfo(
        name = written.name,
        rank = written.ranking,
        profileImage = if (written.profileImage is ProfileImage.Web) written.profileImage.url else "",
        uid = written.uid
    ),
    postInfo = PostInfo(
        title = title,
        postType = category.toStringKR() ?: "",
        view = views,
        like = likeUser.size.toLong(),
        comment = commentCount,
        id = id
    ),
    postImage = imagesUrl.getOrNull(0) ?: "",
    post = text,
    postTime = createdElapsedDateTime,
    postType = category
)


fun Post.toCommunityWritingListItem(views: Long, commentNumber: Long) = CommunityWritingItem(
    userInfo = UserInfo(
        name = written.name,
        rank = written.ranking,
        profileImage = if (written.profileImage is ProfileImage.Web) written.profileImage.url else "",
        uid = written.uid
    ),
    postInfo = PostInfo(
        title = title,
        postType = category.toStringKR() ?: "",
        view = views,
        like = likeUser.size.toLong(),
        comment = commentNumber,
        id = id
    ),
    postImage = "",
    post = text,
    postTime = modifiedElapsedDateTime,
    postType = category
)