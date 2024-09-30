package com.agvber.kekkek.presentation.search.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.databinding.RecyclerviewSearchPostBinding
import com.agvber.kekkek.presentation.mapper.getResourceString
import com.agvber.kekkek.presentation.toResourceId
import com.agvber.kekkek.presentation.utils.diffutil.PostDiffUtil

class SearchPostRecyclerViewAdapter(
    private val itemClickListener: (Post) -> Unit
) : PagingDataAdapter<Post, SearchPostRecyclerViewAdapter.PostSearchViewHolder>(PostDiffUtil()) {

    class PostSearchViewHolder(
        private val binding: RecyclerviewSearchPostBinding,
        private val itemClickListener: (Post) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.tvSearchPostPostType.text = post.category.getResourceString(itemView.context)
            binding.tvSearchPostName.text = post.written.name
            binding.tvSearchPostTitle.text = post.title
            binding.tvSearchPostPost.text = post.text
            binding.tvSearchPostTimeStamp.text = post.createdElapsedDateTime.toResourceId(itemView.context)
            binding.tvSearchPostViewNum.text = post.views.toString()
            binding.tvSearchPostLikeNum.text = post.likeUser.size.toString()
            binding.tvSearchPostCommentNum.text = post.commentCount.toString()

            when (post.written.profileImage) {
                is ProfileImage.Default -> {
                    binding.circleIvSearchPostProfile.setImageResource(R.drawable.ic_user_profile_test)
                }
                is ProfileImage.Web -> {
                    binding.circleIvSearchPostProfile.load(post.written.profileImage.url)
                }
            }

            binding.root.setOnClickListener {
                itemClickListener(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostSearchViewHolder {
        val view = RecyclerviewSearchPostBinding.inflate(
            /* inflater = */ LayoutInflater.from(parent.context),
            /* parent = */ parent,
            /* attachToParent = */ false
        )
        return PostSearchViewHolder(view, itemClickListener)
    }

    override fun onBindViewHolder(holder: PostSearchViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }
}