package com.stopsmoke.kekkek.presentation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.domain.model.Comment

class PostCommentAdapter :
    PagingDataAdapter<Comment, PostCommentAdapter.PostCommentViewHolder>(diffUtil) {

    class PostCommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) = with(binding) {
            tvCommentNickname.text = comment.written.name
            tvCommentDescription.text = comment.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentViewHolder {
        val view =
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return PostCommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostCommentViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Comment,
                newItem: Comment,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}