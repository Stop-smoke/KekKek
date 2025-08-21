package com.agvber.kekkek.presentation.reply.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.databinding.ItemCommentBinding
import com.agvber.kekkek.presentation.reply.callback.ReplyCallback
import com.agvber.kekkek.presentation.setDefaultProfileImage
import com.agvber.kekkek.presentation.toResourceId
import com.agvber.kekkek.presentation.utils.handleImageColor

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val callback: ReplyCallback?,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(comment: Comment): Unit = with(binding) {
        tvCommentNickname.text = comment.written.name
        tvCommentDescription.text = comment.text
        tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)
        ivCommentProfile.setDefaultProfileImage(comment.written.profileImage)
        tvCommentLikeNum.text = comment.likeUser.size.toString()

        ivCommentLike.handleImageColor(
            context = itemView.context,
            activeColor = R.color.primary_blue,
            passiveColor = R.color.gray_lightgray2,
            isActive = comment.isLiked
        )

        ivCommentProfile.setOnClickListener {
            callback?.navigateToUserProfile(comment.written.uid)
        }

        clCommentLike.setOnClickListener {
            callback?.setCommentLike(!comment.isLiked)
        }
    }
}