package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.algolia.model.SearchPostEntity
import com.stopsmoke.kekkek.core.domain.model.DateTime
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.PostEdit
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.Written
import com.stopsmoke.kekkek.core.firestore.model.PostEntity
import com.stopsmoke.kekkek.core.firestore.model.WrittenEntity
import com.stopsmoke.kekkek.presentation.mapper.toPostCategory
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
        dateTime = dateTime.asExternalModel(),
        likeUser = likeUser,
        unlikeUser = unlikeUser,
        category = category?.toPostCategory() ?: PostCategory.UNKNOWN,
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
        category = category?.toPostCategory() ?: PostCategory.UNKNOWN,
        views = views ?: 0,
        commentCount = commentCount ?: 0,
        bookmarkUser = bookmarkUser,
        imagesUrl = imagesUrl
    )