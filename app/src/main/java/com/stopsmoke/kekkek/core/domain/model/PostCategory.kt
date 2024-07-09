package com.stopsmoke.kekkek.core.domain.model

enum class PostCategory {
    NOTICE,
    QUIT_SMOKING_SUPPORT,
    POPULAR,
    QUIT_SMOKING_AIDS_REVIEWS,
    SUCCESS_STORIES,
    GENERAL_DISCUSSION,
    FAILURE_STORIES,
    RESOLUTIONS,
    UNKNOWN,
    ALL
}

fun PostCategory.toRequestString(): String? = when (this) {
    PostCategory.NOTICE -> "notice"
    PostCategory.QUIT_SMOKING_SUPPORT -> "quit_smoking_support"
    PostCategory.POPULAR -> "popular"
    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
    PostCategory.SUCCESS_STORIES -> "success_stories"
    PostCategory.GENERAL_DISCUSSION -> "general_discussion"
    PostCategory.FAILURE_STORIES -> "failure_stories"
    PostCategory.RESOLUTIONS -> "resolutions"
    PostCategory.UNKNOWN -> null
    PostCategory.ALL -> null
}

fun PostCategory.toStringKR(): String? = when (this) {
    PostCategory.NOTICE -> "공지사항"
    PostCategory.QUIT_SMOKING_SUPPORT -> " 금연 지원 프로그램 공지"
    PostCategory.POPULAR -> "인기글"
    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "금연 보조제 후기"
    PostCategory.SUCCESS_STORIES -> "금연 성공 후기"
    PostCategory.GENERAL_DISCUSSION -> "자유 게시판"
    PostCategory.FAILURE_STORIES -> "금연 실패 후기"
    PostCategory.RESOLUTIONS -> "금연 다짐"
    PostCategory.UNKNOWN -> null
    PostCategory.ALL -> "커뮤니티 홈"
}

fun String.toPostCategory(): PostCategory = when (this) {
    "notice", "공지사항" -> PostCategory.NOTICE
    "quit_smoking_support", "금연 지원 프로그램 공지" -> PostCategory.QUIT_SMOKING_SUPPORT
    "popular", "인기글" -> PostCategory.POPULAR
    "quit_smoking_aids_reviews", "금연 보조제 후기" -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
    "success_stories", "금연 성공 후기" -> PostCategory.SUCCESS_STORIES
    "general_discussion", "자유 게시판" -> PostCategory.GENERAL_DISCUSSION
    "failure_stories", "금연 실패 후기" -> PostCategory.FAILURE_STORIES
    "resolutions", "금연 다짐" -> PostCategory.RESOLUTIONS
    "all", "커뮤니티 홈" -> PostCategory.ALL
    else -> PostCategory.UNKNOWN
}