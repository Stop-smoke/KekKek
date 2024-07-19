package com.stopsmoke.kekkek.presentation.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemReplyBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewEmptyBinding
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.RecyclerviewEmptyViewHolder
import com.stopsmoke.kekkek.presentation.reply.callback.ReplyCallback
import com.stopsmoke.kekkek.presentation.reply.viewholder.ReplyViewHolder
import com.stopsmoke.kekkek.presentation.utils.diffutil.ReplyUiStateDiffUtil

private enum class PreviewReplyViewType {
   REPLY, DELETED, ERROR
}

class PreviewReplyAdapter : ListAdapter<ReplyUiState, RecyclerView.ViewHolder>(ReplyUiStateDiffUtil()) {

    private var callback: ReplyCallback? = null

    fun registerCallback(replyCallback: ReplyCallback) {
        callback = replyCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            PreviewReplyViewType.REPLY.ordinal -> {
                val view = ItemReplyBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                ReplyViewHolder(view, callback)
            }

            else -> {
                val view = RecyclerviewEmptyBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                RecyclerviewEmptyViewHolder(view)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = getItem(position) ?: return

        when (holder) {
            is ReplyViewHolder -> {
                holder.bind((currentItem as ReplyUiState.ReplyType).data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ReplyUiState.ReplyType -> PreviewReplyViewType.REPLY.ordinal
            is ReplyUiState.ItemDeleted -> PreviewReplyViewType.DELETED.ordinal
            null -> PreviewReplyViewType.ERROR.ordinal
        }
    }
}