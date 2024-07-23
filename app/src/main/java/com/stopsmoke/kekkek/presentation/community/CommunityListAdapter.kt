package com.stopsmoke.kekkek.presentation.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.getRelativeTime
import com.stopsmoke.kekkek.presentation.mapper.toStringKR
import com.stopsmoke.kekkek.presentation.utils.diffutil.CommunityWritingItemDiffUtil

class CommunityListAdapter :
    PagingDataAdapter<CommunityWritingItem, CommunityListAdapter.ViewHolder>(
        CommunityWritingItemDiffUtil()
    ) {

    private var callback: CommunityCallbackListener? = null

    fun registerCallbackListener(callback: CommunityCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: CommunityWritingItem)
    }

    class WritingPostViewHolder(
        private val binding: ItemPostBinding,
        private val callback: CommunityCallbackListener?,
    ) : ViewHolder(binding.root) {

        override fun bind(item: CommunityWritingItem): Unit = with(binding) {
            item.postInfo.let {
                tvItemWritingTitle.text = it.title
                tvItemWritingViewNum.text = it.view.toString()
                tvItemWritingLikeNum.text = it.like.toString()
                tvItemWritingCommentNum.text = it.comment.toString()
            }

            tvItemWritingPost.text = item.post
            tvItemWritingTimeStamp.text = getRelativeTime(item.postTime)

            item.userInfo.let {
                it.profileImage.let { imgUrl ->
                    if (imgUrl.isNullOrBlank()) circleIvItemWritingProfile.setImageResource(R.drawable.ic_user_profile_test)
                    else circleIvItemWritingProfile.load(imgUrl)
                }
                tvItemWritingName.text = it.name
                tvItemWritingRank.text = "랭킹 ${it.rank}위"
            }

            item.postImage.let {
                if (it.isNotBlank()) {
                    ivItemWritingPostImage.load(it) {
                        crossfade(true)
                    }
                    ivItemWritingPostImage.visibility = View.VISIBLE
                } else {
                    ivItemWritingPostImage.visibility = View.GONE
                    setMarginEnd(tvItemWritingTitle, 16)
                }
            }

            tvItemWritingPostType.text = item.postType.toStringKR() ?: ""

            binding.circleIvItemWritingProfile.setOnClickListener {
                callback?.navigateToUserProfile(item.userInfo.uid)
            }

            binding.root.setOnClickListener {
                callback?.navigateToPost(item.postInfo.id)
            }
        }


        private fun setMarginEnd(view: TextView, end: Int) {
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginEnd = end
            view.layoutParams = layoutParams
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder =
        WritingPostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            callback
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}
