package com.stopsmoke.kekkek.presentation.reply.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.presentation.reply.callback.ReplyCallback
import com.stopsmoke.kekkek.presentation.setDefaultProfileImage
import com.stopsmoke.kekkek.presentation.toResourceId
import com.stopsmoke.kekkek.presentation.utils.handleImageColor

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