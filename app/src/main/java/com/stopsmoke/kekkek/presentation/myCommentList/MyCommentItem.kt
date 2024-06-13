package com.stopsmoke.kekkek.presentation.myCommentList

import com.stopsmoke.kekkek.domain.model.CommentPostData
import java.time.LocalDateTime


data class MyCommentItem(
    val postData: CommentPostData,
    val commentId: String,
    val content: String,
    val time: LocalDateTime
)
