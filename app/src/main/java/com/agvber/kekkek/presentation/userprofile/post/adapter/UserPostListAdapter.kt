package com.agvber.kekkek.presentation.userprofile.post.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.databinding.ItemPostBinding
import com.agvber.kekkek.presentation.toResourceId
import com.agvber.kekkek.presentation.utils.diffutil.PostDiffUtil

class UserPostListAdapter(
    private val itemClick: (Post) -> Unit,
) : PagingDataAdapter<Post, UserPostListAdapter.UserPostViewHolder>(PostDiffUtil()) {

    override fun onBindViewHolder(holder: UserPostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPostViewHolder {
        val view =
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return UserPostViewHolder(view, itemClick)
    }

    class UserPostViewHolder(
        val binding: ItemPostBinding,
        private val itemClick: (Post) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

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

            binding.root.setOnClickListener {
                itemClick(post)
            }
        }
    }
}