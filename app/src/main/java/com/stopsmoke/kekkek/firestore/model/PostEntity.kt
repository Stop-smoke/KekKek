package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class PostEntity(
    @get:PropertyName("id")
    val id: String? = null,

    @get:PropertyName("comment_id")
    @set:PropertyName("comment_id")
    var commentId: String? = null,

    @get:PropertyName("written_uid")
    @set:PropertyName("written_uid")
    var writtenUid: String? = null,

    @get:PropertyName("title")
    val title: String? = null,

    @get:PropertyName("text")
    val text: String? = null,

    @get:PropertyName("date_time")
    @set:PropertyName("date_time")
    var dateTime: String? = null,

    @get:PropertyName("bookmark")
    val bookmark: Int? = null,

    @get:PropertyName("is_bookmark")
    @set:PropertyName("is_bookmark")
    var isBookmark: Boolean? = null,

    @get:PropertyName("like")
    val like: Int? = null,

    @get:PropertyName("is_like")
    @set:PropertyName("is_like")
    var isLike: Boolean? = null,

    @get:PropertyName("unlike")
    val unlike: Int? = null,

    @get:PropertyName("is_unlike")
    @set:PropertyName("is_unlike")
    var isUnlike: Boolean? = null,
)