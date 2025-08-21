package com.agvber.kekkek.core.domain.model

import com.agvber.kekkek.core.data.mapper.asExternalModel
import com.agvber.kekkek.core.domain.getElapsedDateTime
import com.agvber.kekkek.core.firestore.model.ReplyEntity

data class Reply(
    val id: String,
    val written: Written,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val dateTime: DateTime,
    val text: String,
    val commentParent: CommentParent,
    var replyParent: String,
    val isLiked: Boolean
) {
    val elapsedCreatedDateTime = dateTime.created.getElapsedDateTime()
}

fun emptyReply()= ReplyEntity().asExternalModel(false)
