package com.agvber.kekkek.presentation.reply.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.Reply
import com.agvber.kekkek.databinding.ItemReplyBinding
import com.agvber.kekkek.presentation.reply.callback.ReplyCallback
import com.agvber.kekkek.presentation.setDefaultProfileImage
import com.agvber.kekkek.presentation.toResourceId
import com.agvber.kekkek.presentation.utils.handleImageColor

class ReplyViewHolder(
    private val binding: ItemReplyBinding,
    private val callback: ReplyCallback?,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(reply: Reply) = with(binding) {
        tvCommentNickname.text = reply.written.name
        tvCommentDescription.text = reply.text
        tvCommentHour.text = reply.elapsedCreatedDateTime.toResourceId(itemView.context)
        ivCommentProfile.setDefaultProfileImage(reply.written.profileImage)
        tvCommentLikeNum.text = reply.likeUser.size.toString()
        ivCommentLike.handleImageColor(
            context = itemView.context,
            activeColor = R.color.primary_blue,
            passiveColor = R.color.gray_lightgray2,
            isActive = reply.isLiked
        )

        ivCommentProfile.setOnClickListener {
            callback?.navigateToUserProfile(reply.written.uid)
        }

        clCommentLike.setOnClickListener {
            callback?.setReplyLike(reply.id, !reply.isLiked)
        }

        itemView.setOnLongClickListener {
            callback?.deleteItem(reply)
            true
        }
    }
}