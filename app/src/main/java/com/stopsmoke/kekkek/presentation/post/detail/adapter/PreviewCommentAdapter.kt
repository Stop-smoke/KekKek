package com.stopsmoke.kekkek.presentation.post.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PreviewCommentHolder

class PreviewCommentAdapter : ListAdapter<Comment, PreviewCommentHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewCommentHolder {
        val view = ItemCommentBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return PreviewCommentHolder(view)
    }

    override fun onBindViewHolder(holder: PreviewCommentHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem === newItem
            }

        }
    }

}