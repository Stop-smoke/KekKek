package com.stopsmoke.kekkek.presentation.post.reply.callback

import com.stopsmoke.kekkek.domain.model.Reply

interface ReplyDialogCallback {
    fun deleteReply(reply: Reply)
}