package com.stopsmoke.kekkek.presentation.myBookmarkList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.databinding.ItemCommunityPostwritingBinding
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class BookmarkListAdapter
    : PagingDataAdapter<CommunityWritingItem, BookmarkListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CommunityWritingItem>() {
        override fun areItemsTheSame(
            oldItem: CommunityWritingItem,
            newItem: CommunityWritingItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CommunityWritingItem,
            newItem: CommunityWritingItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    private var callback: CommunityCallbackListener? = null

    fun registerCallbackListener(callback: CommunityCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }


    class ViewHolder(
        private val binding: ItemCommunityPostwritingBinding,
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

                binding.root.setOnClickListener {
                    callback?.navigateToPost(item)
                }
            }
        }

        private fun getRelativeTime(pastTime: ElapsedDateTime): String {
            val timeType = when (pastTime.elapsedDateTime) {
                DateTimeUnit.YEAR -> "년"
                DateTimeUnit.MONTH -> "달"
                DateTimeUnit.DAY -> "일"
                DateTimeUnit.WEEK -> "주"
                DateTimeUnit.HOUR -> "시간"
                DateTimeUnit.MINUTE -> "분"
                DateTimeUnit.SECOND -> "초"
            }

            return "${pastTime.number} ${timeType} 전"
        }

        private fun setMarginEnd(view: TextView, end: Int) {
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginEnd = end
            view.layoutParams = layoutParams
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkListAdapter.ViewHolder {
        return ViewHolder(
            ItemCommunityPostwritingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback
        )
    }

    override fun onBindViewHolder(holder: BookmarkListAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}