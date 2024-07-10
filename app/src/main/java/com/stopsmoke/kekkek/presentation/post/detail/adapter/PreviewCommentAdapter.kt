package com.stopsmoke.kekkek.presentation.post.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PreviewCommentHolder
import com.stopsmoke.kekkek.presentation.utils.diffutil.CommentDiffUtil

class PreviewCommentAdapter : ListAdapter<Comment, PreviewCommentHolder>(CommentDiffUtil()) {

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
}