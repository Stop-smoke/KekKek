package com.agvber.kekkek.presentation.post.detail.model

import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.User

data class PostContentItem(
    val user: User?,
    val post: Post?,
)