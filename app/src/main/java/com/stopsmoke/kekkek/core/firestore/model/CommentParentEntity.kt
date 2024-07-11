package com.stopsmoke.kekkek.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class CommentParentEntity (
    @get:PropertyName("post_id") @set:PropertyName("post_id")
    var postId: String? = null,

    @get:PropertyName("post_title") @set:PropertyName("post_title")
    var postTitle: String? = null,

    @get:PropertyName("post_type") @set:PropertyName("post_type")
    var postType: String? = null,
)