package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.core.domain.model.Reply

class ReplyDiffUtil : DiffUtil.ItemCallback<Reply>() {
    override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
        return oldItem === newItem
    }
}