package com.stopsmoke.kekkek.domain.model

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

fun String.toPostCategory(): PostCategory = when (this) {
    "notice" -> PostCategory.NOTICE
    "quit_smoking_support" -> PostCategory.QUIT_SMOKING_SUPPORT
    "popular" -> PostCategory.POPULAR
    "quit_smoking_aids_reviews" -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
    "success_stories" -> PostCategory.SUCCESS_STORIES
    "general_discussion" -> PostCategory.GENERAL_DISCUSSION
    "failure_stories" -> PostCategory.FAILURE_STORIES
    "resolutions" -> PostCategory.RESOLUTIONS
    "all" -> PostCategory.ALL
    else -> PostCategory.UNKNOWN
}