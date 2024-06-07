package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class CommentEntity(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String? = null,

    @get:PropertyName("post_id") @set:PropertyName("post_id")
    var postId: String? = null,

    @get:PropertyName("text") @set:PropertyName("text")
    var text: String? = null,

    @get:PropertyName("date_time") @set:PropertyName("date_time")
    var dateTime: DateTimeEntity? = null,

    @get:PropertyName("like_user") @set:PropertyName("like_user")
    var likeUser: List<String> = emptyList(),

    @get:PropertyName("unlike_user") @set:PropertyName("unlike_user")
    var unlikeUser: List<String> = emptyList(),

    @get:PropertyName("reply") @set:PropertyName("reply")
    var reply: ReplyEntity? = null,

    @get:PropertyName("written") @set:PropertyName("written")
    var written: WrittenEntity? = null
)
