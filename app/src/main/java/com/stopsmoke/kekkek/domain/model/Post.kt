package com.stopsmoke.kekkek.domain.model

import com.stopsmoke.kekkek.domain.getElapsedDateTime

data class Post(
    val id: String,
    val commentId: String,
    val written: Written,
    val title: String,
    val text: String,
    val dateTime: DateTime,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val categories: PostCategory,
    val views: Long,
    val commentUser: List<String>,
) {
    val modifiedElapsedDateTime = dateTime.modified.getElapsedDateTime()

}