package com.agvber.kekkek.presentation.post.detail.viewholder

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.core.domain.model.Reply
import com.agvber.kekkek.databinding.RecyclerviewPostViewReplyBinding
import com.agvber.kekkek.presentation.post.detail.callback.PostCommentCallback
import com.agvber.kekkek.presentation.setDefaultProfileImage
import com.agvber.kekkek.presentation.toResourceId

class PostReplyViewHolder(
    private val binding: RecyclerviewPostViewReplyBinding,
    private val callback: PostCommentCallback?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Comment) {
        initReplyMoreView(item)
        initFirstReplyItem(item.earliestReply.getOrNull(0))
        initSecondReplyItem(item.earliestReply.getOrNull(1))
        initThirdReplyItem(item.earliestReply.getOrNull(2))

        binding.includeReply1.root.setOnLongClickListener {
            callback?.deleteReply(commentId = item.id, item.earliestReply[0].id)
            true
        }
        binding.includeReply2.root.setOnLongClickListener {
            callback?.deleteReply(commentId = item.id, item.earliestReply[1].id)
            true
        }
        binding.includeReply3.root.setOnLongClickListener {
            callback?.deleteReply(commentId = item.id, item.earliestReply[2].id)
            true
        }
    }

    private fun initReplyMoreView(comment: Comment) {
        if (comment.earliestReply.size > 3) {
            binding.llReplyMore.visibility = View.VISIBLE
            binding.tvReplyMore.text = itemView.context.getString(
                /* resId = */ R.string.post_view_reply_more,
                /* ...formatArgs = */ comment.replyCount - 3
            )
            clickReplyItemMore(comment)
        } else {
            binding.llReplyMore.visibility = View.GONE
        }
    }

    private fun clickReplyItemMore(comment: Comment) {
        binding.llReplyMore.setOnClickListener {
            callback?.navigateToReply(comment)
        }
    }

    private fun initFirstReplyItem(reply: Reply?) {
        if (reply == null) {
            binding.groupReply1.visibility = View.GONE
            return
        }

        binding.groupReply1.visibility = View.VISIBLE
        binding.includeReply1.tvCommentNickname.text = reply.written.name
        binding.includeReply1.tvCommentDescription.text = reply.text
        binding.includeReply1.tvCommentHour.text = reply.elapsedCreatedDateTime.toResourceId(itemView.context)
        binding.includeReply1.ivCommentProfile.setDefaultProfileImage(reply.written.profileImage)
        binding.includeReply1.tvCommentLikeNum.text = reply.likeUser.size.toString()
        binding.includeReply1.ivCommentLike.setLikeButtonColor(reply.isLiked)
        binding.includeReply1.clCommentLike.setOnClickListener {
            callback?.clickReplyLike(reply)
        }
    }

    private fun initSecondReplyItem(reply: Reply?) {
        if (reply == null) {
            binding.groupReply2.visibility = View.GONE
            return
        }

        binding.groupReply2.visibility = View.VISIBLE
        binding.includeReply2.tvCommentNickname.text = reply.written.name
        binding.includeReply2.tvCommentDescription.text = reply.text
        binding.includeReply2.tvCommentHour.text = reply.elapsedCreatedDateTime.toResourceId(itemView.context)
        binding.includeReply2.ivCommentProfile.setDefaultProfileImage(reply.written.profileImage)
        binding.includeReply2.tvCommentLikeNum.text = reply.likeUser.size.toString()
        binding.includeReply2.ivCommentLike.setLikeButtonColor(reply.isLiked)
        binding.includeReply2.clCommentLike.setOnClickListener {
            callback?.clickReplyLike(reply)
        }
    }

    private fun initThirdReplyItem(reply: Reply?) {
        if (reply == null) {
            binding.groupReply3.visibility = View.GONE
            return
        }

        binding.groupReply3.visibility = View.VISIBLE
        binding.includeReply3.tvCommentNickname.text = reply.written.name
        binding.includeReply3.tvCommentDescription.text = reply.text
        binding.includeReply3.tvCommentHour.text = reply.elapsedCreatedDateTime.toResourceId(itemView.context)
        binding.includeReply3.ivCommentProfile.setDefaultProfileImage(reply.written.profileImage)
        binding.includeReply3.tvCommentLikeNum.text = reply.likeUser.size.toString()
        binding.includeReply3.ivCommentLike.setLikeButtonColor(reply.isLiked)
        binding.includeReply3.clCommentLike.setOnClickListener {
            callback?.clickReplyLike(reply)
        }
    }

    private fun ImageView.setLikeButtonColor(isLike: Boolean) {
        if (isLike) {
            setColorFilter(
                ContextCompat.getColor(itemView.context, R.color.primary_blue)
            )
            return
        }

        setColorFilter(
            ContextCompat.getColor(itemView.context, R.color.gray_lightgray2)
        )
    }

}