package com.stopsmoke.kekkek.presentation.post.detail.model

import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.User

data class PostContentItem(
    val user: User?,
    val post: Post?,
)