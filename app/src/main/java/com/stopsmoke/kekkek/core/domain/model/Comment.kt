package com.stopsmoke.kekkek.core.domain.model

import com.stopsmoke.kekkek.core.domain.getElapsedDateTime
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
    val replyCount: Long,
    val isLiked: Boolean
) {
    val elapsedCreatedDateTime = dateTime.created.getElapsedDateTime()
}

fun emptyComment() = Comment(
    id = "",
    text = "",
    dateTime = DateTime(
        LocalDateTime.now(),
        LocalDateTime.now()
    ),
    likeUser = emptyList(),
    unlikeUser = emptyList(),
    earliestReply = emptyList(),
    written = Written(
        "",
        "",
        ProfileImage.Default,
        0
    ),
    parent = CommentParent(PostCategory.UNKNOWN, "", ""),
    replyCount = 0,
    isLiked = false
)