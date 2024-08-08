package com.stopsmoke.kekkek.presentation.my.bookmark

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.core.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.core.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.mapper.getResourceString

class BookmarkListViewHolder(
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
            tvItemWritingPostType.text = item.postType.getResourceString(binding.root.context)
            tvItemWritingName.text = it.name
            tvItemWritingRank.text = "랭킹 ${it.rank}위"

            circleIvItemWritingProfile.load(it.profileImage)

            binding.circleIvItemWritingProfile.setOnClickListener {
                callback?.navigateToUserProfile(item.userInfo.uid)
            }

            binding.root.setOnClickListener {
                callback?.navigateToPost(item.postInfo.id)
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