package com.stopsmoke.kekkek.presentation.reply.callback

import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.Reply

interface ReplyCallback {
    fun deleteItem(reply: Reply)

    fun updateReply(reply: Reply)

    fun commentLikeClick(comment: Comment)

    fun navigateToUserProfile(uid: String)
}