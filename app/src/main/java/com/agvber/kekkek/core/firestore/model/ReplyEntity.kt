package com.agvber.kekkek.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class ReplyEntity(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String? = null,

    @get:PropertyName("written") @set:PropertyName("written")
    var written: WrittenEntity? = null,

    @get:PropertyName("like_user") @set:PropertyName("like_user")
    var likeUser: List<String> = emptyList(),

    @get:PropertyName("unlike_user") @set:PropertyName("unlike_user")
    var unlikeUser: List<String> = emptyList(),

    @get:PropertyName("date_time") @set:PropertyName("date_time")
    var dateTime: DateTimeEntity? = null,

    @get:PropertyName("text") @set:PropertyName("text")
    var text: String? = null,

    @get:PropertyName("comment_parent") @set:PropertyName("comment_parent")
    var commentParent: CommentParentEntity? = null,

    @get:PropertyName("reply_parent") @set:PropertyName("reply_parent")
    var replyParent: String? = null,
)
