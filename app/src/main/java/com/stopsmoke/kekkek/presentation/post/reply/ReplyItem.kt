package com.stopsmoke.kekkek.presentation.post.reply

import android.os.Parcelable
import com.stopsmoke.kekkek.domain.getElapsedDateTime
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentParent
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.Written
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

sealed class ReplyRecyclerviewItem {
    data class ReplyRecyclerviewReply(
        val id: String,
        val written: Written,
        val likeUser: List<String>,
        val unlikeUser: List<String>,
        val dateTime: DateTime,
        val text: String,
        val commentParent: CommentParent,
        var replyParent: String,
    ) : ReplyRecyclerviewItem() {
        val elapsedCreatedDateTime = dateTime.created.getElapsedDateTime()
    }

    data class ReplyRecyclerviewComment(
        val id: String,
        val text: String,
        val dateTime: DateTime,
        val likeUser: List<String>,
        val unlikeUser: List<String>,
        val reply: List<Reply>,
        val written: Written,
        val parent: CommentParent
    ) : ReplyRecyclerviewItem()
}

fun ReplyRecyclerviewItem.ReplyRecyclerviewReply.toReply() = Reply(
    id = id,
    written = written,
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    dateTime = dateTime,
    text = text,
    commentParent = commentParent,
    replyParent = replyParent,
)

fun ReplyRecyclerviewItem.ReplyRecyclerviewComment.toComment() = Comment(
    id = id,
    text = text,
    dateTime = dateTime,
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    reply = reply,
    written = written,
    parent = parent
)

fun Reply.toReplyRecyclerviewItem() = ReplyRecyclerviewItem.ReplyRecyclerviewReply(
    id = id,
    written = written,
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    dateTime = dateTime,
    text = text,
    commentParent = commentParent,
    replyParent = replyParent,
)

fun Comment.toReplyRecyclerviewItem() = ReplyRecyclerviewItem.ReplyRecyclerviewComment(
    id = id,
    text = text,
    dateTime = dateTime,
    likeUser = likeUser,
    unlikeUser = unlikeUser,
    reply = reply,
    written = written,
    parent = parent
)
