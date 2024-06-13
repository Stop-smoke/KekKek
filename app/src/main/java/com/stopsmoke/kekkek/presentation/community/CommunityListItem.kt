package com.stopsmoke.kekkek.presentation.community

import android.os.Parcelable
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
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



fun Post.toCommunityWritingListItem() = CommunityWritingItem(
    userInfo = UserInfo(
        name = written.name,
        rank = written.ranking,
        profileImage = if (written.profileImage is ProfileImage.Web) written.profileImage.url else "",
        uid = written.uid
    ),
    postInfo = PostInfo(
        title = title,
        postType = when (categories) {
            PostCategory.NOTICE -> "공지사항"
            PostCategory.QUIT_SMOKING_SUPPORT -> " 금연 지원 프로그램 공지"
            PostCategory.POPULAR -> "인기글"
            PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "금연 보조제 후기"
            PostCategory.SUCCESS_STORIES -> "금연 성공 후기"
            PostCategory.GENERAL_DISCUSSION -> "자유게시판"
            PostCategory.FAILURE_STORIES -> "금연 실패 후기"
            PostCategory.RESOLUTIONS -> "금연 다짐"
            PostCategory.UNKNOWN -> ""
            PostCategory.ALL -> ""
        },
        view = views,
        like = likeUser.size.toLong(),
        comment = commentUser.size.toLong(),
        id = id
    ),
    postImage = "",
    post = text,
    postTime = modifiedElapsedDateTime,
    postType = categories
)


