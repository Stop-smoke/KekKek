package com.stopsmoke.kekkek.domain.model

import java.io.InputStream

data class PostWrite(
    val title: String,
    val content: String,
    val category: PostWriteCategory,
    val image: InputStream
)