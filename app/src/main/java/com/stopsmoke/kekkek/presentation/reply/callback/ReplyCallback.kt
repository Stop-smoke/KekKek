package com.stopsmoke.kekkek.presentation.reply.callback

import com.stopsmoke.kekkek.core.domain.model.Reply

interface ReplyCallback {
    fun deleteItem(reply: Reply)

    fun navigateToUserProfile(uid: String)

    fun setCommentLike(like: Boolean)

    fun setReplyLike(id: String, like: Boolean)
}