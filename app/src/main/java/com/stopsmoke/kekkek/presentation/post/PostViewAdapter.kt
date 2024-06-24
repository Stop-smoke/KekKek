package com.stopsmoke.kekkek.presentation.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewPostviewContentBinding
import com.stopsmoke.kekkek.databinding.UnknownItemBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import com.stopsmoke.kekkek.presentation.toResourceId
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class PostViewAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: PostViewModel
) :
    PagingDataAdapter<Comment, PostViewAdapter.ViewHolder>(diffUtil) {

    private var callback: PostCommentCallback? = null

    fun registerCallback(postCommentCallback: PostCommentCallback) {
        callback = postCommentCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    enum class PostViewType {
        CONTENT, COMMENT
    }

    abstract class ViewHolder(
        root: View
    ) : RecyclerView.ViewHolder(root) {
        abstract fun bind(comment: Comment)
    }

    inner class PostContentViewHolder(
        private val binding: RecyclerviewPostviewContentBinding
    ) : ViewHolder(binding.root) {
        override fun bind(comment: Comment) {
            binding.ivPostPoster.setOnClickListener {
                val post = viewModel.post.value ?: return@setOnClickListener
                binding.root.findNavController().navigate(
                    R.id.action_post_view_to_user_profile, bundleOf(
                        "uid" to (post.written.uid)
                    )
                )
            }

            binding.clPostViewHeart.setOnClickListener {
                viewModel.toggleLikeToPost()
            }

            lifecycleOwner.lifecycleScope.launch {
                combine(viewModel.post, viewModel.user) { post, user ->
                    if (user !is User.Registered) return@combine
                    if (post == null) return@combine

                    if (user.uid in viewModel.post.value!!.likeUser) {
                        binding.ivPostHeart.setImageResource(R.drawable.ic_heart_filled)
                    } else {
                        binding.ivPostHeart.setImageResource(R.drawable.ic_heart)
                    }
                }
                    .collect()
            }
            initPostView(itemView.context)
            observeCommentCount()
        }

        private fun initPostView(context: Context) = with(binding) {
            viewModel.post.collectLatestWithLifecycle(lifecycleOwner.lifecycle) { post ->
                if (post == null) return@collectLatestWithLifecycle

                tvPostPosterNickname.text = post.written.name
                tvPostPosterRanking.text = "랭킹 ${post.written.ranking}위"
                tvPostHour.text = post.createdElapsedDateTime.toResourceId(context)

                tvPostTitle.text = post.title
                tvPostDescription.text = post.text
                tvPostHeartNum.text = post.likeUser.size.toString()
                tvPostCommentNum.text = post.commentCount.toString()
                tvPostViewNum.text = post.views.toString()
                initWrittenProfileImage(post.written.profileImage)
            }
        }

        private fun initWrittenProfileImage(profileImage: ProfileImage) {
            when (profileImage) {
                is ProfileImage.Web -> binding.ivPostPoster.load(profileImage.url)
                is ProfileImage.Default -> binding.ivPostView.setImageResource(R.drawable.ic_user_profile_test)
            }
        }

        private fun observeCommentCount() {
            viewModel.commentCount.collectLatestWithLifecycle(lifecycleOwner.lifecycle) {
                binding.tvPostCommentNum.text = it.toString()
            }
        }
    }

    inner class PostCommentViewHolder(
        val binding: ItemCommentBinding
    ) : ViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> callback?.deleteItem(it1) }
                true
            }
        }

        override fun bind(comment: Comment) = with(binding) {
            tvCommentNickname.text = comment.written.name
            tvCommentDescription.text = comment.text
            tvCommentHour.text = comment.elapsedCreatedDateTime.toResourceId(itemView.context)

            comment.written.profileImage.let { profileImage ->
                when (profileImage) {
                    is ProfileImage.Web -> ivCommentProfile.load(profileImage.url)
                    is ProfileImage.Default -> ivCommentProfile.setImageResource(R.drawable.ic_user_profile_test)
                }
            }

            ivCommentProfile.setOnClickListener {
                callback?.navigateToUserProfile(comment.written.uid)
            }


            tvCommentLikeNum.text = comment.likeUser.size.toString()
            val userUid = (viewModel.user.value as? User.Registered)?.uid ?: ""
            val isLikeUser:Boolean = userUid in comment.likeUser
            if(isLikeUser) ivCommentLike.setColorFilter(ContextCompat.getColor(itemView.context, R.color.primary_blue))
            else ivCommentLike.setColorFilter(ContextCompat.getColor(itemView.context, R.color.gray_lightgray2))


            clCommentLike.setOnClickListener {
                val list = comment.likeUser.toMutableList()
                if(isLikeUser) list.remove(userUid) else list.add(userUid)
                callback?.commentLikeClick(comment.copy(
                    likeUser = list
                ))
            }

            clCommentRecomment.setOnClickListener {

            }
        }
    }

    class UnknownViewHolder(
        binding: UnknownItemBinding
    ) : ViewHolder(binding.root) {
        override fun bind(comment: Comment) {}
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) PostViewType.CONTENT.ordinal else PostViewType.COMMENT.ordinal
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            PostViewType.CONTENT.ordinal -> {
                PostContentViewHolder(
                    RecyclerviewPostviewContentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            PostViewType.COMMENT.ordinal -> {
                PostCommentViewHolder(
                    ItemCommentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                UnknownViewHolder(
                    UnknownItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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