package com.stopsmoke.kekkek.presentation.search.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.RecyclerviewSearchPostBinding
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.presentation.getResourceString
import com.stopsmoke.kekkek.presentation.toResourceId
import com.stopsmoke.kekkek.presentation.utils.diffutil.PostDiffUtil

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