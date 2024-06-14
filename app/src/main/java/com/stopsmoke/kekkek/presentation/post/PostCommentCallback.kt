package com.stopsmoke.kekkek.presentation.post

import com.stopsmoke.kekkek.domain.model.Comment

interface PostCommentCallback {

    fun deleteItem(comment: Comment)

    fun navigateToUserProfile(uid: String)

}