package com.stopsmoke.kekkek.presentation.community

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ItemCommunityPostcategoryBinding
import com.stopsmoke.kekkek.databinding.ItemCommunityPostpopularBinding
import com.stopsmoke.kekkek.databinding.ItemCommunityPostwritingBinding
import com.stopsmoke.kekkek.databinding.ItemRecyclerviewCommunityCategoryBinding
import com.stopsmoke.kekkek.databinding.ItemUnknownBinding
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class CommunityListAdapter
    : ListAdapter<CommunityListItem, CommunityListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CommunityListItem>() {
        override fun areItemsTheSame(
            oldItem: CommunityListItem,
            newItem: CommunityListItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CommunityListItem,
            newItem: CommunityListItem
        ): Boolean {
            return oldItem == newItem
        }

    }
) {
    enum class PostItemViewType {
        POPULAR, CATEGORY, POST
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: CommunityListItem)
    }


    class PopularPostViewHolder(
        private val binding: ItemCommunityPostpopularBinding
    ) : ViewHolder(binding.root) {
        override fun bind(item: CommunityListItem) = with(binding) {
            val popularItem: CommunityListItem.CommunityPopularItem =
                item as CommunityListItem.CommunityPopularItem

            popularItem.postInfo1.let {
                tvItemPopularTitle1.text = it.title
                tvItemPopularViewNum1.text = it.view.toString()
                tvItemPopularLikeNum1.text = it.like.toString()
                tvItemPopularCommentNum1.text = it.comment.toString()
                tvItemPopularPostType1.text = it.postType
            }

            popularItem.postInfo2.let {
                tvItemPopularTitle2.text = it.title
                tvItemPopularViewNum2.text = it.view.toString()
                tvItemPopularLikeNum2.text = it.like.toString()
                tvItemPopularCommentNum2.text = it.comment.toString()
                tvItemPopularPostType2.text = it.postType
            }

            tvItemPopularFullView.setOnClickListener {
                // 전체보기로 이동 추가
            }
        }
    }

    class WritingPostViewHolder(
        private val binding: ItemCommunityPostwritingBinding
    ) : ViewHolder(binding.root) {
        override fun bind(item: CommunityListItem): Unit = with(binding) {
            val writingItem: CommunityListItem.CommunityWritingItem =
                item as CommunityListItem.CommunityWritingItem

            writingItem.postInfo.let {
                tvItemWritingTitle.text = it.title
                tvItemWritingViewNum.text = it.view.toString()
                tvItemWritingLikeNum.text = it.like.toString()
                tvItemWritingCommentNum.text = it.comment.toString()
            }

            tvItemWritingPost.text = writingItem.post
            tvItemWritingTimeStamp.text = getRelativeTime(writingItem.postTime)

            writingItem.userInfo.let {
                if (writingItem.postImage != "") {
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

    class CategoryViewHolder(
        private val binding: ItemCommunityPostcategoryBinding
    ) : ViewHolder(binding.root) {
        override fun bind(item: CommunityListItem) = with(binding) {
            val categoryItem: CommunityListItem.CommunityCategoryItem =
                item as CommunityListItem.CommunityCategoryItem

            rvItemPostCategoryCategory.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = CommunityCategoryListAdapter()
            adapter.submitList(categoryItem.categoryList)
            rvItemPostCategoryCategory.adapter = adapter
        }
    }

    class UnknownViewHolder(
        binding: ItemUnknownBinding
    ) : ViewHolder(binding.root) {
        override fun bind(item: CommunityListItem) {
        }
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is CommunityListItem.CommunityPopularItem -> PostItemViewType.POPULAR.ordinal
        is CommunityListItem.CommunityWritingItem -> PostItemViewType.POST.ordinal
        is CommunityListItem.CommunityCategoryItem -> PostItemViewType.CATEGORY.ordinal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityListAdapter.ViewHolder = when (viewType) {
        PostItemViewType.POPULAR.ordinal -> {
            PopularPostViewHolder(
                ItemCommunityPostpopularBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        PostItemViewType.POST.ordinal -> {
            WritingPostViewHolder(
                ItemCommunityPostwritingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        PostItemViewType.CATEGORY.ordinal -> {
            CategoryViewHolder(
                ItemCommunityPostcategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        else -> {
            UnknownViewHolder(
                ItemUnknownBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: CommunityListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun isHeader(position: Int): Boolean =
        getItemViewType(position) == PostItemViewType.CATEGORY.ordinal

    fun getHeaderView(list: RecyclerView, position: Int): View? {
        val lastIndex = if (position < currentList.size) position else currentList.size - 1

        for (index in lastIndex downTo 0) {
            if (getItemViewType(index) == PostItemViewType.CATEGORY.ordinal) {
                val titleItem = getItem(position) as? CommunityListItem.CommunityCategoryItem
                if (titleItem != null) {
                    val binding = ItemCommunityPostcategoryBinding.inflate(
                        LayoutInflater.from(list.context),
                        list,
                        false
                    )
                    return binding.root
                }
            }
        }
        return null
    }
}