package com.agvber.kekkek.presentation.post.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.agvber.kekkek.databinding.ItemPostBinding
import com.agvber.kekkek.presentation.community.CommunityCallbackListener
import com.agvber.kekkek.presentation.community.CommunityWritingItem
import com.agvber.kekkek.presentation.getRelativeTime
import com.agvber.kekkek.presentation.mapper.toStringKR
import com.agvber.kekkek.presentation.utils.diffutil.CommunityWritingItemDiffUtil

class NoticeListAdapter
    : PagingDataAdapter<CommunityWritingItem, NoticeListAdapter.ViewHolder>(
    CommunityWritingItemDiffUtil()
) {

    private var callback: CommunityCallbackListener? = null

    fun registerCallbackListener(callback: CommunityCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }


    class ViewHolder(
        private val binding: ItemPostBinding,
        private val callback: CommunityCallbackListener?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunityWritingItem) = with(binding) {
            item.postInfo.let {
                tvItemWritingTitle.text = it.title
                tvItemWritingViewNum.text = it.view.toString()
                tvItemWritingLikeNum.text = it.like.toString()
                tvItemWritingCommentNum.text = it.comment.toString()
            }

            tvItemWritingPost.text = item.post
            tvItemWritingTimeStamp.text = getRelativeTime(item.postTime)

            item.userInfo.let {
                if (item.postImage != "") {
                    ivItemWritingPostImage.load(it.profileImage) {
                        crossfade(true)
//                    placeholder(R.drawable.placeholder) 로딩중 띄우나?
//                    error(R.drawable.error) 오류시 띄우나?
                    }
                } else {
                    ivItemWritingPostImage.visibility = View.GONE
                    setMarginEnd(tvItemWritingTitle, 16)
                }

                tvItemWritingName.text = it.name
                tvItemWritingRank.text = "랭킹 ${it.rank}위"

                circleIvItemWritingProfile.load(it.profileImage)
            }

            tvItemWritingPostType.text = item.postType.toStringKR()

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
    ): ViewHolder {
        return ViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}