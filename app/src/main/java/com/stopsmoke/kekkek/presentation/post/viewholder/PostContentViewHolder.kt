package com.stopsmoke.kekkek.presentation.post.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.RecyclerviewPostviewContentBinding
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.post.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.post.model.PostContentItem
import com.stopsmoke.kekkek.presentation.snackbarLongShow
import com.stopsmoke.kekkek.presentation.toResourceId

class PostContentViewHolder(
    private val binding: RecyclerviewPostviewContentBinding,
    private val callback: PostCommentCallback?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(headerItem: PostContentItem) {
        initUserProfileView(headerItem.user, headerItem.post)
        initPostView(headerItem.post)
        clickPostLike(headerItem.post)

        binding.ivPostPoster.setOnClickListener {
            headerItem.post?.let { post ->
                callback?.navigateToUserProfile(post.written.uid)
            }
        }
    }

    private fun clickPostLike(post: Post?) {
        binding.clPostViewHeart.setOnClickListener {
            if (post == null) {
                return@setOnClickListener
            }
            callback?.clickPostLike(post)
        }
    }

    private fun initUserProfileView(user: User?, post: Post?) {
        if (user == null || post == null) {
            return
        }

        when (user) {
            is User.Error -> {
                itemView.snackbarLongShow("유저 정보 에러")
            }

            is User.Guest -> {}
            is User.Registered -> {
                if (post.likeUser.contains(user.uid)) {
                    binding.ivPostHeart.setImageResource(R.drawable.ic_heart_filled)
                } else {
                    binding.ivPostHeart.setImageResource(R.drawable.ic_heart)
                }
            }
        }
    }

    private fun initPostView(post: Post?) = with(binding) {
        if (post == null) return@with

        tvPostPosterNickname.text = post.written.name
        tvPostPosterRanking.text = "랭킹 ${post.written.ranking}위"
        tvPostHour.text = post.modifiedElapsedDateTime.toResourceId(itemView.context)

        tvPostTitle.text = post.title
        tvPostDescription.text = post.text
        tvPostHeartNum.text = post.likeUser.size.toString()
        tvPostCommentNum.text = post.commentCount.toString()
        tvPostViewNum.text = post.views.toString()
        initWrittenProfileImage(post.written.profileImage)
        binding.tvPostCommentNum.text = post.commentCount.toString()

        if (post.imagesUrl.isNotEmpty()) {
            binding.ivPostViewImage.load(post.imagesUrl[0])
            binding.ivPostViewImage.visibility = View.VISIBLE
        }
    }

    private fun initWrittenProfileImage(profileImage: ProfileImage) {
        when (profileImage) {
            is ProfileImage.Web -> binding.ivPostPoster.load(profileImage.url)
            is ProfileImage.Default -> binding.ivPostView.setImageResource(R.drawable.ic_user_profile_test)
        }
    }
}