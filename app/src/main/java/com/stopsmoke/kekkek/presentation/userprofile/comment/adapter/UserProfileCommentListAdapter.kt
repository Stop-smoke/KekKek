package com.stopsmoke.kekkek.presentation.userprofile.comment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.RecyclerviewUserProfileCommentBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentParent
import com.stopsmoke.kekkek.presentation.getResourceString

class UserProfileCommentListAdapter(
    private val itemClick: (Comment) -> Unit
) : PagingDataAdapter<Comment, UserProfileCommentListAdapter.CommentViewHolder>(diffUtil) {

    class CommentViewHolder(
        private val binding: RecyclerviewUserProfileCommentBinding,
        private val itemClick: (Comment) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.ivUserProfileCommentIcon.setImageResource(R.drawable.ic_notification_chat)
            binding.tvUserProfileCommentTitle.text = comment.text
            binding.tvUserProfileCommentDateTime.text =
                comment.dateTime.modified.run {
                    "$year-${"%02d".format(monthValue)}-${"%02d".format(dayOfMonth)}"
                }
            binding.tvUserProfileCommentBody.text = itemView.context.getCommentStateString(comment.parent)

            binding.root.setOnClickListener {
                itemClick(comment)
            }
        }
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view =
            RecyclerviewUserProfileCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CommentViewHolder(view, itemClick)
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }
}

private fun Context.getCommentStateString(item: CommentParent): String =
    "${item.postType.getResourceString(this)}에 등록한 ${item.postTitle} 게시글에 댓글을 남겼습니다."