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

fun String.toPostWriteCategory() = when(this){
    "자유 게시판" -> PostWriteCategory.GENERAL_DISCUSSION
    "금연 성공 후기" -> PostWriteCategory.SUCCESS_STORIES
    "금연 보조제 후기" -> PostWriteCategory.QUIT_SMOKING_AIDS_REVIEWS
    "금연 실패 후기" -> PostWriteCategory.FAILURE_STORIES
    "금연 다짐" -> PostWriteCategory.QUIT_SMOKING_WILLINGNESS
    else -> throw IllegalStateException()
}