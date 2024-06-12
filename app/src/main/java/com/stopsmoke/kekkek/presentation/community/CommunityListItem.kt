package com.stopsmoke.kekkek.presentation.community

import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage

data class CommunityWritingItem(
    val userInfo: UserInfo,
    val postInfo: PostInfo,
    val postImage: String,
    val post: String,
    val postTime: ElapsedDateTime,
    val postType: PostCategory
)

data class CommunityPopularItem(
    val postInfo1: PostInfo,
    val postInfo2: PostInfo
)

data class PostInfo(
    val title: String,
    val postType: String,
    val view: Long,
    val like: Long,
    val comment: Long
)

data class UserInfo(
    val name: String,
    val rank: Long,
    val profileImage: String
)

fun Post.toCommunityWritingListItem() =  CommunityWritingItem(
    userInfo = UserInfo(
        name = written.name,
        rank = written.ranking,
        profileImage = if (written.profileImage is ProfileImage.Web) written.profileImage.url else ""
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
        comment = commentUser.size.toLong()
    ),
    postImage = "",
    post = text,
    postTime = modifiedElapsedDateTime,
    postType = categories
)


