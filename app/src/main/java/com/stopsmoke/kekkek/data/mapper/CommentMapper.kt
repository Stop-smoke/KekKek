package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.toPostCategory
import com.stopsmoke.kekkek.domain.model.toRequestString
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.CommentPostDataEntity
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import com.stopsmoke.kekkek.firestore.model.WrittenEntity

internal fun CommentEntity.asExternalModel(): Comment =
    Comment(
        id = id ?: "null",
        text = text ?: "null",
        dateTime = dateTime.asExternalModel(),
        likeUser = likeUser,
        unlikeUser = unlikeUser,
        reply = reply.map { it.asExternalModel() },
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

internal fun Comment.toEntity(): CommentEntity = CommentEntity(
    id = id,
    text = text,
    dateTime = dateTime.toEntity(),
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    reply = reply.map {
        ReplyEntity(
            written = WrittenEntity(
                uid = it.written.uid,
                name = it.written.name,
                profileImage = (it.written.profileImage as? ProfileImage.Web)?.url,
                ranking = it.written.ranking
            ),
            likeUser = it.likeUser,
            unlikeUser = it.unlikeUser,
            dateTime = it.dateTime.toEntity(),
            text = text
        )
    },
    written = WrittenEntity(
        uid = written.uid,
        name = written.name,
        profileImage = (written.profileImage as? ProfileImage.Web)?.url,
        ranking = written.ranking
    ),
    CommentPostDataEntity(
        postType = postData.postType.toRequestString(),
        postId = postData.postId,
        postTitle = postData.postTitle
    )
)