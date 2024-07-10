package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.core.domain.model.Comment

class CommentDiffUtil : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(
        oldItem: Comment,
        newItem: Comment,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Comment,
        newItem: Comment,
    ): Boolean {
        return oldItem == newItem
    }
}