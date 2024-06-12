package com.stopsmoke.kekkek.presentation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewExamplePagingBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.firestore.model.CommentEntity

private class PostCommentAdapter :
    PagingDataAdapter<Comment, PostCommentAdapter.PostCommentViewHolder>(diffUtil) {

    class PostCommentViewHolder(val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostCommentViewHolder {
        val view =
            RecyclerviewExamplePagingBinding.inflate(
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
        private val diffUtil = object : DiffUtil.ItemCallback<CommentEntity>() {
            override fun areItemsTheSame(oldItem: CommentEntity, newItem: CommentEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: CommentEntity,
                newItem: CommentEntity,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}