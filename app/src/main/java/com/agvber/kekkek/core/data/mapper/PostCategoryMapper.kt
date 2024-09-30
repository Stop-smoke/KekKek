package com.agvber.kekkek.core.data.mapper

import com.agvber.kekkek.core.domain.model.PostCategory
import com.agvber.kekkek.core.domain.model.PostWriteCategory

fun String.toPostCategory(): PostCategory = when (this) {
    "notice", "공지사항" -> PostCategory.NOTICE
    "quit_smoking_aids_reviews", "금연 보조제 후기" -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
    "success_stories", "금연 성공 후기" -> PostCategory.SUCCESS_STORIES
    "general_discussion", "자유 게시판" -> PostCategory.GENERAL_DISCUSSION
    "resolutions", "금연 다짐" -> PostCategory.RESOLUTIONS
    "all", "커뮤니티 홈" -> PostCategory.ALL
    else -> PostCategory.UNKNOWN
}

fun PostCategory.toRequestString(): String? = when (this) {
    PostCategory.NOTICE -> "notice"
    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
    PostCategory.SUCCESS_STORIES -> "success_stories"
    PostCategory.GENERAL_DISCUSSION -> "general_discussion"
    PostCategory.RESOLUTIONS -> "resolutions"
    PostCategory.UNKNOWN -> null
    PostCategory.ALL -> null
}

fun PostWriteCategory.toRequestString(): String = when(this) {
    PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
    PostWriteCategory.SUCCESS_STORIES -> "success_stories"
    PostWriteCategory.GENERAL_DISCUSSION -> "general_discussion"
    PostWriteCategory.RESOLUTIONS -> "resolutions"
}