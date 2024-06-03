package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class CommentEntity(
    @PropertyName("id")
    val id: String? = null,
    @PropertyName("title")
    val title: String? = null,
    @PropertyName("text")
    val text: String? = null,
    @get:PropertyName("date_time") @set:PropertyName("date_time")
    var dateTime: String? = null,
    @PropertyName("like")
    val like: Int? = null,
    @get:PropertyName("is_like") @set:PropertyName("is_like")
    var isLike: Boolean? = null,
    @PropertyName("unlike")
    val unlike: Int? = null,
    @get:PropertyName("is_unlike") @set:PropertyName("is_unlike")
    var isUnlike: Boolean? = null,
)
