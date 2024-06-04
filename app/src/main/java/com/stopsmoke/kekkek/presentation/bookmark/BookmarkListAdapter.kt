package com.stopsmoke.kekkek.presentation.bookmark

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import coil.load
import com.stopsmoke.kekkek.databinding.FragmentBookmarkBinding
import com.stopsmoke.kekkek.databinding.ItemCommunityPostwritingBinding
import com.stopsmoke.kekkek.presentation.community.CommunityListItem
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class BookmarkListAdapter
    : ListAdapter<BookmarkWritingItem, BookmarkListAdapter.ViewHolder>(
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

        private fun getRelativeTime(pastDate: Date): String {
            val now = Calendar.getInstance().time
            val diffInMillis = now.time - pastDate.time

            val seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            val months = days / 30
            val years = days / 365

            return when {
                seconds < 60 -> "${seconds}초 전"
                minutes < 60 -> "${minutes}분 전"
                hours < 24 -> "${hours}시간 전"
                days < 30 -> "${days}일 전"
                months < 12 -> "${months}달 전"
                else -> "${years}년 전"
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
        holder.bind(getItem(position))
    }
}