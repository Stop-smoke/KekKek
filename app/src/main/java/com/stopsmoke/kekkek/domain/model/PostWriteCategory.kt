package com.stopsmoke.kekkek.domain.model

enum class PostWriteCategory {
    GENERAL_DISCUSSION,
    SUCCESS_STORIES,
    QUIT_SMOKING_AIDS_REVIEWS,
    FAILURE_STORIES,
    QUIT_SMOKING_WILLINGNESS
}

fun PostWriteCategory.toRequestString(): String = when(this) {
    PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
    PostWriteCategory.SUCCESS_STORIES -> "success_stories"
    PostWriteCategory.GENERAL_DISCUSSION -> "general_discussion"
    PostWriteCategory.FAILURE_STORIES -> "failure_stories"
    PostWriteCategory.QUIT_SMOKING_WILLINGNESS -> "resolutions"
}