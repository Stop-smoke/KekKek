package com.stopsmoke.kekkek.presentation.post.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.presentation.post.detail.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PostCommentViewHolder
import com.stopsmoke.kekkek.presentation.utils.diffutil.CommentDiffUtil

class PreviewCommentAdapter : ListAdapter<Comment, PostCommentViewHolder>(CommentDiffUtil()) {

    private var callback: PostCommentCallback? = null

    fun registerCallback(postCommentCallback: PostCommentCallback) {
        callback = postCommentCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentViewHolder {
        val view = ItemCommentBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return PostCommentViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: PostCommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}