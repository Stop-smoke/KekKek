package com.agvber.kekkek.core.data.mapper

import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.core.domain.model.CommentParent
import com.agvber.kekkek.core.domain.model.PostCategory
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.core.domain.model.Reply
import com.agvber.kekkek.core.firestore.model.CommentEntity
import com.agvber.kekkek.core.firestore.model.CommentParentEntity
import com.agvber.kekkek.core.firestore.model.ReplyEntity
import com.agvber.kekkek.core.firestore.model.WrittenEntity

internal fun CommentEntity.asExternalModel(
    earliestReply: List<Reply>,
    isLiked: Boolean,
): Comment = Comment(
    id = id ?: "null",
    text = text ?: "null",
    dateTime = dateTime.asExternalModel(),
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    earliestReply = earliestReply,
    written = written.asExternalModel(),
    parent = parent?.asExternalModel() ?: CommentParent(
        postType = PostCategory.UNKNOWN,
        postId = "null",
        postTitle = "null"
    ),
    replyCount = replyCount ?: 0,
    isLiked = isLiked
)

internal fun CommentParentEntity.asExternalModel(): CommentParent =
    CommentParent(
        postId = postId ?: "null",
        postTitle = postTitle ?: "null",
        postType = postType?.toPostCategory() ?: PostCategory.UNKNOWN
    )

internal fun CommentParent.toEntity(): CommentParentEntity =
    CommentParentEntity(
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
    earliestReply = earliestReply.map {
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
    parent = parent.toEntity(),
    replyCount = replyCount
)