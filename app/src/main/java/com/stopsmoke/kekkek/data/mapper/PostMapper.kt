package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.firestore.model.PostEntity
import java.time.LocalDateTime

fun PostEntity.asExternalModel(): Post =
    Post(
        id = id ?: "null",
        commentId = commentId ?: "null",
        written = Post.Written(
            uid = written?.uid ?: "null",
            name = written?.name ?: "null",
            profileImage = written?.profileImage
                ?.let { ProfileImage.Web(it) }
                ?: ProfileImage.Default,
            ranking = written?.ranking ?: -1,
        ),
        title = title ?: "null",
        text = text ?: "null",
        dateTime = DateTime(
            created = dateTime?.created?.toLocalDateTime() ?: LocalDateTime.MIN,
            modified = dateTime?.modified?.toLocalDateTime() ?: LocalDateTime.MIN
        ),
        likeUser = likeUser,
        unlikeUser = unlikeUser,
        categories = when (categories) {
            "notice" -> PostCategory.NOTICE
            "quit_smoking_support" -> PostCategory.QUIT_SMOKING_SUPPORT
            "popular" -> PostCategory.POPULAR
            "quit_smoking_aids_reviews" -> PostCategory.QUIT_SMOKING_AIDS_REVIEWS
            "success_stories" -> PostCategory.SUCCESS_STORIES
            "general_discussion" -> PostCategory.GENERAL_DISCUSSION
            "failure_stories" -> PostCategory.FAILURE_STORIES
            "resolutions" -> PostCategory.RESOLUTIONS
            else -> PostCategory.UNKNOWN
        },
        views = views ?: 0,
        commentUser = commentUser
    )