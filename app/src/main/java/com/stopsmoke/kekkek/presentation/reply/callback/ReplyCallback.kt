package com.stopsmoke.kekkek.presentation.reply.callback

import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.Reply

interface ReplyCallback {
    fun deleteItem(reply: Reply)

    fun updateReply(reply: Reply)

    fun commentLikeClick(comment: Comment)

    fun navigateToUserProfile(uid: String)
}