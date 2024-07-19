package com.stopsmoke.kekkek.presentation.reply.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.databinding.ItemReplyBinding
import com.stopsmoke.kekkek.presentation.reply.callback.ReplyCallback
import com.stopsmoke.kekkek.presentation.setDefaultProfileImage
import com.stopsmoke.kekkek.presentation.toResourceId
import com.stopsmoke.kekkek.presentation.utils.handleImageColor

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