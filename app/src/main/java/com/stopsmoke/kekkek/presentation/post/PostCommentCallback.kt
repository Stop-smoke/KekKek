package com.stopsmoke.kekkek.presentation.post

import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.Reply

interface PostCommentCallback {

    fun deleteItem(comment: Comment)

    fun navigateToUserProfile(uid: String)

    fun commentLikeClick(comment: Comment){}
    fun commentLikeClick(reply: Reply){}

    fun navigateToReply(comment: Comment)
}