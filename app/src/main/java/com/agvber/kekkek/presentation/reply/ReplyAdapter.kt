package com.agvber.kekkek.presentation.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.databinding.ItemCommentBinding
import com.agvber.kekkek.databinding.ItemReplyBinding
import com.agvber.kekkek.databinding.RecyclerviewEmptyBinding
import com.agvber.kekkek.presentation.post.detail.viewholder.RecyclerviewEmptyViewHolder
import com.agvber.kekkek.presentation.reply.callback.ReplyCallback
import com.agvber.kekkek.presentation.reply.viewholder.CommentViewHolder
import com.agvber.kekkek.presentation.reply.viewholder.ReplyViewHolder
import com.agvber.kekkek.presentation.utils.diffutil.ReplyUiStateDiffUtil

private enum class ReplyViewType {
    COMMENT, REPLY, DELETED, ERROR
}

class ReplyAdapter :
    PagingDataAdapter<ReplyUiState, RecyclerView.ViewHolder>(ReplyUiStateDiffUtil()) {

    private var callback: ReplyCallback? = null

    fun registerCallback(replyCallback: ReplyCallback) {
        callback = replyCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    private var comment: Comment? = null

    fun updateComment(value: Comment) {
        comment = value
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ReplyViewType.COMMENT.ordinal -> {
                val view = ItemCommentBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                CommentViewHolder(view, callback)
            }

            ReplyViewType.REPLY.ordinal -> {
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
            is CommentViewHolder -> {
                comment?.let { holder.bind(it) }
            }

            is ReplyViewHolder -> {
                holder.bind((currentItem as ReplyUiState.ReplyType).data)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return ReplyViewType.COMMENT.ordinal
        }

        return when (getItem(position)) {
            is ReplyUiState.ReplyType -> ReplyViewType.REPLY.ordinal
            is ReplyUiState.ItemDeleted -> ReplyViewType.DELETED.ordinal
            null -> ReplyViewType.ERROR.ordinal
        }
    }
}