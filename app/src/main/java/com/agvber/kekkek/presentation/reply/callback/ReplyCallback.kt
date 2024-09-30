package com.agvber.kekkek.presentation.reply.callback

import com.agvber.kekkek.core.domain.model.Reply

interface ReplyCallback {
    fun deleteItem(reply: Reply)

    fun navigateToUserProfile(uid: String)

    fun setCommentLike(like: Boolean)

    fun setReplyLike(id: String, like: Boolean)
}