package com.stopsmoke.kekkek.presentation.post.model

import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.User

data class PostContentItem(
    val user: User?,
    val post: Post?,
    val commentNum: Long,
)