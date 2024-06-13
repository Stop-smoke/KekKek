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
import com.stopsmoke.kekkek.getRelativeTime
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class BookmarkListAdapter
    : PagingDataAdapter<BookmarkWritingItem, BookmarkListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<BookmarkWritingItem>() {
        override fun areItemsTheSame(
            oldItem: BookmarkWritingItem,
            newItem: BookmarkWritingItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BookmarkWritingItem,
            newItem: BookmarkWritingItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    class ViewHolder(
        private val binding: ItemCommunityPostwritingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BookmarkWritingItem) = with(binding) {
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
        viewType: Int
    ): BookmarkListAdapter.ViewHolder {
        return ViewHolder(
            ItemCommunityPostwritingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BookmarkListAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}