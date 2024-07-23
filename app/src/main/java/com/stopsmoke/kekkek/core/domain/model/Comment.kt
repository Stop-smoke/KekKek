package com.stopsmoke.kekkek.core.domain.model

import com.stopsmoke.kekkek.core.domain.getElapsedDateTime

data class Comment(
    val id: String,
    val text: String,
    val dateTime: DateTime,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val earliestReply: List<Reply>,
    val written: Written,
    val parent: CommentParent,
    val replyCount: Long,
    val isLiked: Boolean
) {
    val elapsedCreatedDateTime = dateTime.created.getElapsedDateTime()
}