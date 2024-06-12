package com.stopsmoke.kekkek.domain.model

import com.google.firebase.firestore.PropertyName

data class CommentPostData (
    var postType: PostCategory,
    var postId: String,
    var postTitle: String,
)