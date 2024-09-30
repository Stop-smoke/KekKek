package com.agvber.kekkek.presentation.post.detail.callback

import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.Reply

interface PostCommentCallback {

    fun deleteReply(commentId: String, replyId: String)

    fun deleteItem(comment: Comment)
    fun navigateToUserProfile(uid: String)
    fun navigateToReply(comment: Comment)
    fun commentLikeClick(comment: Comment)

    fun clickPostLike(post: Post)

    fun clickReplyLike(reply: Reply)
}
