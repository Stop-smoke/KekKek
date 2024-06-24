package com.stopsmoke.kekkek.presentation.post.reply

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.databinding.ItemReplyBinding
import com.stopsmoke.kekkek.databinding.UnknownItemBinding
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.toResourceId

class ReplyListAdapter(
    private val viewModel: ReplyViewModel
) : PagingDataAdapter<Reply, ReplyListAdapter.ViewHolder>(diffCallback) {

    abstract class ViewHolder(
        root: View
    ) : RecyclerView.ViewHolder(root) {
        abstract fun bind(reply: Reply)
    }

    enum class ViewType {
        COMMENT, REPLY
    }

    inner class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : ViewHolder(binding.root) {
        override fun bind(reply: Reply): Unit = with(binding) {
        }
    }

    class ReplyViewHolder(
        binding: ItemReplyBinding
    ) : ViewHolder(binding.root) {
        override fun bind(reply: Reply) {
        }

    }

    class UnknownViewHolder(
        binding: UnknownItemBinding
    ) : ViewHolder(binding.root) {
        override fun bind(reply: Reply) {}
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ViewType.COMMENT.ordinal
        else ViewType.REPLY.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            ViewType.COMMENT.ordinal -> CommentViewHolder(
                ItemCommentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            ViewType.REPLY.ordinal -> ReplyViewHolder(
                ItemReplyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> UnknownViewHolder(
                UnknownItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Reply>() {
            override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem.id == newItem.id

            }
        }
    }
}