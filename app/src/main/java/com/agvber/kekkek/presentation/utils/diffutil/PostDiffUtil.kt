package com.agvber.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.agvber.kekkek.core.domain.model.Post

class PostDiffUtil : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(
        oldItem: Post,
        newItem: Post,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Post,
        newItem: Post,
    ): Boolean {
        return oldItem === newItem
    }
}