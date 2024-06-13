package com.stopsmoke.kekkek.presentation.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.presentation.toResourceId

class PostCommentAdapter :
    PagingDataAdapter<Comment, PostCommentAdapter.PostCommentViewHolder>(diffUtil) {

    private var callback: PostCommentCallback? = null

    fun registerCallback(postCommentCallback: PostCommentCallback) {
        callback = postCommentCallback
    }

    fun unregisterCallback() {
        callback = null
    }


    inner class PostCommentViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {

                binding.root.setOnLongClickListener {

                    getItem(bindingAdapterPosition)?.id?.let { it1 -> callback?.deleteItem(it1) }

                    true
                }

            }

        fun bind(comment: Comment) = with(binding) {
            tvCommentNickname.text = comment.written.name
            tvCommentDescription.text = comment.text
            tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)
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