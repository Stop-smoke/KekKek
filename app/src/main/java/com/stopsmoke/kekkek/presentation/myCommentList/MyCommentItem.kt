package com.stopsmoke.kekkek.presentation.myCommentList

import com.stopsmoke.kekkek.domain.model.CommentParent
import java.time.LocalDateTime


data class MyCommentItem(
    val postData: CommentParent,
    val commentId: String,
    val content: String,
    val time: LocalDateTime
)
