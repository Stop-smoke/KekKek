package com.stopsmoke.kekkek.domain.model

data class Comment(
    val id: String,
    val text: String,
    val dateTime: DateTime,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val reply: List<Reply>,
    val written: Written,
    val postData: CommentPostData
)