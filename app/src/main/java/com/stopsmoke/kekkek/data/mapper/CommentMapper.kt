package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.firestore.model.CommentEntity

internal fun CommentEntity.asExternalModel(): Comment =
    Comment(
        id = id ?: "null",
        postId = postId ?: "null",
        text = text ?: "null",
        dateTime = dateTime.asExternalModel(),
        likeUser = likeUser,
        unlikeUser = unlikeUser,
        reply = reply.asExternalModel(),
        written = written.asExternalModel()
    )