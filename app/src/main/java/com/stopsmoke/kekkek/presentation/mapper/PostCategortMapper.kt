package com.stopsmoke.kekkek.presentation.mapper

import android.content.Context
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.PostWriteCategory

fun String.toPostCategory(): PostCategory = when (this) {
    "notice", "공지사항" -> PostCategory.NOTICE
    "quit_smoking_aids_reviews", "금연 보조제 후기" -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
    "success_stories", "금연 성공 후기" -> PostCategory.SUCCESS_STORIES
    "general_discussion", "자유 게시판" -> PostCategory.GENERAL_DISCUSSION
    "resolutions", "금연 다짐" -> PostCategory.RESOLUTIONS
    "all", "커뮤니티 홈" -> PostCategory.ALL
    else -> PostCategory.UNKNOWN
}

fun String.toPostWriteCategory() = when(this){
    "자유 게시판" -> PostWriteCategory.GENERAL_DISCUSSION
    "금연 성공 후기" -> PostWriteCategory.SUCCESS_STORIES
    "금연 보조제 후기" -> PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS
    "금연 다짐" -> PostWriteCategory.RESOLUTIONS
    else -> throw IllegalStateException()
}

fun PostWriteCategory.toPostCategory(): PostCategory = when(this){
    PostWriteCategory.GENERAL_DISCUSSION -> PostCategory.GENERAL_DISCUSSION
    PostWriteCategory.SUCCESS_STORIES -> PostCategory.SUCCESS_STORIES
    PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
    PostWriteCategory.RESOLUTIONS -> PostCategory.RESOLUTIONS
    else -> PostCategory.UNKNOWN
}

internal fun PostCategory.getResourceString(context: Context) = when(this) {
    PostCategory.NOTICE -> context.getString(R.string.community_category_notice)
    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> context.getString(R.string.community_category_quit_smoking_aids_reviews)
    PostCategory.SUCCESS_STORIES -> context.getString(R.string.community_category_success_stories)
    PostCategory.GENERAL_DISCUSSION -> context.getString(R.string.community_category_general_discussion)
    PostCategory.RESOLUTIONS -> context.getString(R.string.community_category_resolutions)
    PostCategory.UNKNOWN -> context.getString(R.string.community_category_unknown)
    PostCategory.ALL -> context.getString(R.string.community_category_all)
}
