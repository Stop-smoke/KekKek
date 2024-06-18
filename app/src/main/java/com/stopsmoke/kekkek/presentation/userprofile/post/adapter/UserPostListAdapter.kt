package com.stopsmoke.kekkek.presentation.userprofile.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommunityPostwritingBinding
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.presentation.toResourceId

class UserPostListAdapter :
    PagingDataAdapter<Post, UserPostListAdapter.UserPostViewHolder>(diffUtil) {

    override fun onBindViewHolder(holder: UserPostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {
        val view =
            ItemCommunityPostwritingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return UserPostViewHolder(view)
    }

    class UserPostViewHolder(val binding: ItemCommunityPostwritingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            with(post.written.profileImage) {
                when (this) {
                    is ProfileImage.Default -> binding.circleIvItemWritingProfile.setImageResource(
                        R.drawable.ic_user_profile_test
                    )

                    is ProfileImage.Web -> binding.circleIvItemWritingProfile.load(url)
                }
            }
            binding.tvItemWritingName.text = post.written.name
            binding.tvItemWritingRank.text = "${post.written.ranking}위"
            binding.tvItemWritingTitle.text = post.title
            binding.tvItemWritingPost.text = post.text
            binding.tvItemWritingTimeStamp.text =
                post.modifiedElapsedDateTime.toResourceId(itemView.context)
            binding.tvItemWritingViewNum.text = post.views.toString()
            binding.tvItemWritingLikeNum.text = post.likeUser.size.toString()
            binding.tvItemWritingCommentNum.text = post.commentCount.toString()
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem == newItem
            }

        }
    }
}