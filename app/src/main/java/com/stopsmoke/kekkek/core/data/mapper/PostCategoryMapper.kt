package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.PostWriteCategory

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