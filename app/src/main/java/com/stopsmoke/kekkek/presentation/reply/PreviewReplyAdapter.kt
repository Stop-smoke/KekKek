package com.stopsmoke.kekkek.presentation.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.databinding.ItemReplyBinding
import com.stopsmoke.kekkek.presentation.reply.callback.ReplyCallback
import com.stopsmoke.kekkek.presentation.reply.viewholder.ReplyViewHolder
import com.stopsmoke.kekkek.presentation.utils.diffutil.ReplyDiffUtil

class PreviewReplyAdapter : ListAdapter<Reply, ReplyViewHolder>(ReplyDiffUtil()) {

    private var callback: ReplyCallback? = null

    fun registerCallback(replyCallback: ReplyCallback) {
        callback = replyCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = ItemReplyBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return ReplyViewHolder(view, callback)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return
        holder.bind(currentItem)
    }
}