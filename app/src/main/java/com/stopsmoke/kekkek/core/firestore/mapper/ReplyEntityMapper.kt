package com.stopsmoke.kekkek.core.firestore.mapper

import com.stopsmoke.kekkek.core.firestore.model.InitDateTime
import com.stopsmoke.kekkek.core.firestore.model.InitReplyEntity
import com.stopsmoke.kekkek.core.firestore.model.ReplyEntity

fun ReplyEntity.toInit() = InitReplyEntity(
    id = id,
    written = written,
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    dateTime = InitDateTime(),
    text = text,
    commentParent = commentParent,
    replyParent = replyParent
)