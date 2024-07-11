package com.stopsmoke.kekkek.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class CommentEntity(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String? = null,

    @get:PropertyName("text") @set:PropertyName("text")
    var text: String? = null,

    @get:PropertyName("date_time") @set:PropertyName("date_time")
    var dateTime: DateTimeEntity? = null,

    @get:PropertyName("like_user") @set:PropertyName("like_user")
    var likeUser: List<String> = emptyList(),

    @get:PropertyName("unlike_user") @set:PropertyName("unlike_user")
    var unlikeUser: List<String> = emptyList(),

    @get:PropertyName("earliest_reply") @set:PropertyName("earliest_reply")
    var earliestReply: List<ReplyEntity> = emptyList(),

    @get:PropertyName("written") @set:PropertyName("written")
    var written: WrittenEntity? = null,

    @get:PropertyName("parent") @set:PropertyName("parent")
    var parent: CommentParentEntity? = null,

    @get:PropertyName("reply_count") @set:PropertyName("reply_count")
    var replyCount: Long? = null,
)
