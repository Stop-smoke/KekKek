package com.stopsmoke.kekkek.presentation.post.detail.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.presentation.setDefaultProfileImage
import com.stopsmoke.kekkek.presentation.toResourceId

class PreviewCommentHolder(
    private val binding: ItemCommentBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(comment: Comment) = with(binding) {
        tvCommentNickname.text = comment.written.name
        tvCommentDescription.text = comment.text
        tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)
        binding.ivCommentProfile.setDefaultProfileImage(comment.written.profileImage)
    }
}