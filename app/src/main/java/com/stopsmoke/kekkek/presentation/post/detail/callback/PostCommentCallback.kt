package com.stopsmoke.kekkek.presentation.post.detail.callback

import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.Reply

interface PostCommentCallback {
    fun deleteItem(comment: Comment)
    fun navigateToUserProfile(uid: String)
    fun navigateToReply(comment: Comment)
    fun commentLikeClick(comment: Comment)

    fun clickPostLike(post: Post)

    fun clickReplyLike(reply: Reply)
}
