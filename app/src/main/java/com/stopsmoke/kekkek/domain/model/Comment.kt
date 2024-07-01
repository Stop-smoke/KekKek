package com.stopsmoke.kekkek.domain.model

import com.stopsmoke.kekkek.domain.getElapsedDateTime
import java.time.LocalDateTime

data class Comment(
    val id: String,
    val text: String,
    val dateTime: DateTime,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val earliestReply: List<Reply>,
    val written: Written,
    val parent: CommentParent,
    val replyCount: Long
) {
    val elapsedCreatedDateTime = dateTime.created.getElapsedDateTime()
}

fun emptyComment() = Comment(
    id = "",
    text = "",
    dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
    likeUser = emptyList(),
    unlikeUser = emptyList(),
    earliestReply = emptyList(),
    written = Written("", "", ProfileImage.Default, 0),
    parent = CommentParent(PostCategory.UNKNOWN, "", ""),
    replyCount = 0
)