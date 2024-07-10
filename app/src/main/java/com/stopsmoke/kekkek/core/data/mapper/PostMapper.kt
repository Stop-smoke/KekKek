package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.algolia.model.SearchPostEntity
import com.stopsmoke.kekkek.core.domain.model.DateTime
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.PostEdit
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.Written
import com.stopsmoke.kekkek.core.domain.model.toRequestString
import com.stopsmoke.kekkek.core.firestore.model.PostEntity
import com.stopsmoke.kekkek.core.firestore.model.WrittenEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

fun PostEntity.asExternalModel(): Post =
    Post(
        id = id ?: "null",
        commentId = commentId ?: "null",
        written = Written(
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
        category = when (category) {
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
        commentCount = commentCount ?: 0,
        bookmarkUser = bookmarkUser,
        imagesUrl = imagesUrl
    )

internal fun PostEdit.toEntity(written: Written) = PostEntity(
    written = WrittenEntity(
        uid = written.uid,
        name = written.name,
        profileImage = (written.profileImage as? ProfileImage.Web)?.url,
        ranking = written.ranking,
    ),
    title = title,
    text = text,
    dateTime = dateTime.toEntity(),
    likeUser = emptyList(),
    unlikeUser = emptyList(),
    category = category.toRequestString(),
    views = 0,
)

internal fun Post.toEntity() = PostEntity(
    id = id,
    commentId = commentId,
    written = WrittenEntity(
        uid = written.uid,
        name = written.name,
        profileImage = (written.profileImage as? ProfileImage.Web)?.url,
        ranking = written.ranking,
    ),
    title = title,
    text = text,
    dateTime = dateTime.toEntity(),
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    bookmarkUser = bookmarkUser,
    category = category.toRequestString(),
    views = 0,
    commentCount = commentCount
)

internal fun SearchPostEntity.asExternalModel(): Post =
    Post(
        id = id ?: "null",
        commentId = commentId ?: "null",
        written = Written(
            uid = written?.uid ?: "null",
            name = written?.name ?: "null",
            profileImage = written?.profileImage
                ?.let { ProfileImage.Web(it) }
                ?: ProfileImage.Default,
            ranking = -1,
        ),
        title = title ?: "null",
        text = text ?: "null",
        dateTime = DateTime(
            created = dateTime?.created?.let {
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneOffset.UTC.normalized()
                )
            } ?: LocalDateTime.MIN,
            modified = dateTime?.modified?.let {
                LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneOffset.UTC.normalized()
                )
            } ?: LocalDateTime.MIN
        ),
        likeUser = likeUser,
        unlikeUser = unlikeUser,
        category = when (category) {
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
        commentCount = commentCount ?: 0,
        bookmarkUser = bookmarkUser,
        imagesUrl = imagesUrl
    )