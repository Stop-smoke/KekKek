package com.agvber.kekkek.presentation.reply.callback

import com.agvber.kekkek.core.domain.model.Reply

interface ReplyDialogCallback {
    fun deleteReply(reply: Reply)
}