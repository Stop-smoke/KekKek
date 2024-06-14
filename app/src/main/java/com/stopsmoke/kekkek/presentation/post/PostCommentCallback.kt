package com.stopsmoke.kekkek.presentation.post

interface PostCommentCallback {

    fun deleteItem(commentId: String)

    fun navigateToUserProfile(uid: String)
}