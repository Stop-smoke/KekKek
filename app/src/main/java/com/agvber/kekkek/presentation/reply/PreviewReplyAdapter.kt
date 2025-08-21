package com.agvber.kekkek.presentation.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.agvber.kekkek.core.domain.model.Reply
import com.agvber.kekkek.databinding.ItemReplyBinding
import com.agvber.kekkek.presentation.reply.callback.ReplyCallback
import com.agvber.kekkek.presentation.reply.viewholder.ReplyViewHolder
import com.agvber.kekkek.presentation.utils.diffutil.ReplyDiffUtil

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