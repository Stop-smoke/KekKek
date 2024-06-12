package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.toPostCategory
import com.stopsmoke.kekkek.domain.model.toRequestString
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.CommentPostDataEntity

internal fun CommentEntity.asExternalModel(): Comment =
    Comment(
        id = id ?: "null",
        text = text ?: "null",
        dateTime = dateTime.asExternalModel(),
        likeUser = likeUser,
        unlikeUser = unlikeUser,
        reply = reply.asExternalModel(),
        written = written.asExternalModel(),
        postData = postData?.asExternalModel() ?: CommentPostData(
            PostCategory.UNKNOWN,
            "null",
            "null"
        )
    )

internal fun CommentPostDataEntity.asExternalModel(): CommentPostData =
    CommentPostData(
        postId = postId ?: "null",
        postTitle = postTitle ?: "null",
        postType = postType?.toPostCategory() ?: PostCategory.UNKNOWN
    )

internal fun CommentPostData.toEntity(): CommentPostDataEntity =
    CommentPostDataEntity(
        postId = postId,
        postTitle = postTitle,
        postType = postType.toRequestString()
    )