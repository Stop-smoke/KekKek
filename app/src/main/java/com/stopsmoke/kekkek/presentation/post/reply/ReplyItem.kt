package com.stopsmoke.kekkek.presentation.post.reply

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReplyIdItem(
    val postId: String,
    val commentId: String
) : Parcelable {
    companion object {
        fun init() = ReplyIdItem("", "")
    }
}