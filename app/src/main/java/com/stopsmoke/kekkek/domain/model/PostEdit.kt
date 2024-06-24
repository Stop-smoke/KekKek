package com.stopsmoke.kekkek.domain.model

data class PostEdit(
    val title: String,
    val text: String,
    val dateTime: DateTime,
    val category: PostWriteCategory,
)
