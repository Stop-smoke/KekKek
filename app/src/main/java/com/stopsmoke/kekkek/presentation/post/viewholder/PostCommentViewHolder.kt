package com.stopsmoke.kekkek.presentation.post.viewholder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.presentation.post.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.toResourceId

class PostCommentViewHolder(
    private val binding: ItemCommentBinding,
    private val callback: PostCommentCallback?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment) = with(binding) {
        tvCommentNickname.text = comment.written.name
        tvCommentDescription.text = comment.text
        tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)

        comment.written.profileImage.let { profileImage ->
            when (profileImage) {
                is ProfileImage.Web -> ivCommentProfile.load(profileImage.url)
                is ProfileImage.Default -> ivCommentProfile.setImageResource(R.drawable.ic_user_profile_test)
            }
        }

        binding.root.setOnLongClickListener {
            callback?.deleteItem(comment)
            true
        }

        ivCommentProfile.setOnClickListener {
            callback?.navigateToUserProfile(comment.written.uid)
        }

        tvCommentLikeNum.text = comment.likeUser.size.toString()
        val userUid = ""
        val isLikeUser: Boolean = userUid in comment.likeUser

        if (isLikeUser) {
            ivCommentLike.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.primary_blue
                )
            )
        } else {
            ivCommentLike.setColorFilter(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.gray_lightgray2
                )
            )
        }

        clCommentLike.setOnClickListener {
            val list = comment.likeUser.toMutableList()
            if (isLikeUser) list.remove(userUid) else list.add(userUid)
            callback?.commentLikeClick(
                comment.copy(
                    likeUser = list
                )
            )
        }

        clCommentRecomment.setOnClickListener {
            callback?.navigateToReply(comment)
        }
    }
}