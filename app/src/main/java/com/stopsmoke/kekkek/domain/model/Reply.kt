package com.stopsmoke.kekkek.domain.model

data class Reply(
    val written: Written,
    val likeUser: List<String>,
    val unlikeUser: List<String>,
    val dateTime: DateTime,
    val text: String
)
