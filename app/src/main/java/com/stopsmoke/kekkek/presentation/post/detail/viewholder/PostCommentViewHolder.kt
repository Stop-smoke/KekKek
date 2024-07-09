package com.stopsmoke.kekkek.presentation.post.detail.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.presentation.post.detail.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.setDefaultProfileImage
import com.stopsmoke.kekkek.presentation.toResourceId

class PostCommentViewHolder(
    private val binding: ItemCommentBinding,
    private val callback: PostCommentCallback?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment) = with(binding) {
        initWrittenView(comment)
        initCommentLikeView(comment.isLiked)
        clickCommentLikeView(comment)
        clickProfileImage(comment.written.uid)

        binding.root.setOnLongClickListener {
            callback?.deleteItem(comment)
            true
        }

        clCommentRecomment.setOnClickListener {
            callback?.navigateToReply(comment)
        }
    }

    private fun initWrittenView(comment: Comment) = with(binding) {
        tvCommentNickname.text = comment.written.name
        tvCommentDescription.text = comment.text
        tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)
        ivCommentProfile.setDefaultProfileImage(comment.written.profileImage)
        tvCommentLikeNum.text = comment.likeUser.size.toString()
    }

    private fun initCommentLikeView(isLiked: Boolean) = with(binding) {
        if (isLiked) {
            ivCommentLike.setColorFilter(
                ContextCompat.getColor(itemView.context, R.color.primary_blue)
            )
            return@with
        }

        ivCommentLike.setColorFilter(
            ContextCompat.getColor(itemView.context, R.color.gray_lightgray2)
        )
    }

    private fun clickCommentLikeView(comment: Comment) = with(binding) {
        clCommentLike.setOnClickListener {
            callback?.commentLikeClick(comment)
        }
    }

    private fun clickProfileImage(uid: String) = with(binding) {
        ivCommentProfile.setOnClickListener {
            callback?.navigateToUserProfile(uid)
        }
    }
}