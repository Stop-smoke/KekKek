package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.domain.model.CommentParent
import com.stopsmoke.kekkek.core.domain.model.PostCategory
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.firestore.model.ReplyEntity
import com.stopsmoke.kekkek.core.firestore.model.WrittenEntity

internal fun ReplyEntity?.asExternalModel(isLiked: Boolean): Reply =
    Reply(
        id = this?.id ?: "null",
        written = this?.written.asExternalModel(),
        likeUser = this?.likeUser ?: emptyList(),
        unlikeUser = this?.unlikeUser ?: emptyList(),
        dateTime = this?.dateTime.asExternalModel(),
        text = this?.text ?: "null",
        commentParent = this?.commentParent?.asExternalModel() ?: CommentParent(
            postType = PostCategory.UNKNOWN,
            postId = "null",
            postTitle = "null"
        ),
        replyParent = this?.replyParent ?: "null",
        isLiked = isLiked
    )

internal fun Reply.toEntity(): ReplyEntity =
    ReplyEntity(
        id = id,
        written =  WrittenEntity(
            uid = written.uid,
            name = written.name,
            profileImage = (written.profileImage as? ProfileImage.Web)?.url,
            ranking = written.ranking
        ),
        likeUser = likeUser ,
        unlikeUser = unlikeUser ,
        dateTime = dateTime.toEntity(),
        text = text,
        commentParent = commentParent.toEntity(),
        replyParent = replyParent
    )