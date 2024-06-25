package com.stopsmoke.kekkek.domain.model

import com.stopsmoke.kekkek.domain.getElapsedDateTime
import java.time.LocalDateTime

data class Comment(
    val id: String,
    val text: String,
    val dateTime: DateTime,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val reply: List<Reply>,
    val written: Written,
    val parent: CommentParent
) {
    val elapsedCreatedDateTime = dateTime.created.getElapsedDateTime()
}

fun emptyComment() = Comment(
    id = "",
    text = "",
    dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
    likeUser = emptyList(),
    unlikeUser = emptyList(),
    reply = emptyList(),
    written = Written("", "", ProfileImage.Default, 0),
    parent = CommentParent(PostCategory.UNKNOWN, "", "")
)