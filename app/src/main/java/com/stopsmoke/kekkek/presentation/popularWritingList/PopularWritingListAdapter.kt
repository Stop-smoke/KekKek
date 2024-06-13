package com.stopsmoke.kekkek.presentation.popularWritingList

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
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem

class PopularWritingListAdapter
    : ListAdapter<CommunityWritingItem, PopularWritingListAdapter.ViewHolder>(
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
            }

            tvItemWritingPostType.text = when (item.postType) {
                PostCategory.NOTICE -> "공지사항"
                PostCategory.QUIT_SMOKING_SUPPORT -> "금연 지원 프로그램 공지"
                PostCategory.POPULAR -> "인기글"
                PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "금연 보조제 후기"
                PostCategory.SUCCESS_STORIES -> "금연 성공 후기"
                PostCategory.GENERAL_DISCUSSION -> "자유게시판"
                PostCategory.FAILURE_STORIES -> "금연 실패 후기"
                PostCategory.RESOLUTIONS -> "금연 다짐"
                PostCategory.UNKNOWN -> ""
                PostCategory.ALL -> ""
            }

            binding.circleIvItemWritingProfile.setOnClickListener {
                callback?.navigateToUserProfile(item.userInfo.uid)
            }

            binding.root.setOnClickListener {
                callback?.navigateToPost(item)
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
    ): ViewHolder {
        return ViewHolder(
            ItemCommunityPostwritingBinding.inflate(
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