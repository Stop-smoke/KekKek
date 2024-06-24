package com.stopsmoke.kekkek.domain.model

enum class PostWriteCategory {
    GENERAL_DISCUSSION,
    SUCCESS_STORIES,
    QUIT_SMOKING_AIDS_REVIEWS,
    FAILURE_STORIES,
    RESOLUTIONS
}

fun PostWriteCategory.toRequestString(): String = when(this) {
    PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
    PostWriteCategory.SUCCESS_STORIES -> "success_stories"
    PostWriteCategory.GENERAL_DISCUSSION -> "general_discussion"
    PostWriteCategory.FAILURE_STORIES -> "failure_stories"
    PostWriteCategory.RESOLUTIONS -> "resolutions"
}

fun String.toPostWriteCategory() = when(this){
    "자유 게시판" -> PostWriteCategory.GENERAL_DISCUSSION
    "금연 성공 후기" -> PostWriteCategory.SUCCESS_STORIES
    "금연 보조제 후기" -> PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS
    "금연 실패 후기" -> PostWriteCategory.FAILURE_STORIES
    "금연 다짐" -> PostWriteCategory.RESOLUTIONS
    else -> throw IllegalStateException()
}

fun PostWriteCategory.toPostCategory(): PostCategory = when(this){
    PostWriteCategory.GENERAL_DISCUSSION -> PostCategory.GENERAL_DISCUSSION
    PostWriteCategory.SUCCESS_STORIES -> PostCategory.SUCCESS_STORIES
    PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
    PostWriteCategory.FAILURE_STORIES -> PostCategory.FAILURE_STORIES
    PostWriteCategory.RESOLUTIONS -> PostCategory.RESOLUTIONS
    else -> PostCategory.UNKNOWN
}