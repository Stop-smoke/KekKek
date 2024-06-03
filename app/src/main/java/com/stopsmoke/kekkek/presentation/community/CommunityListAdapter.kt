package com.stopsmoke.kekkek.presentation.community

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommunityPostpopularBinding
import com.stopsmoke.kekkek.databinding.ItemCommunityPostwritingBinding

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
    enum class PostItemViewType{
        POPULAR, CATEGORY, POST
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: CommunityListItem)
    }


    class PopularPostViewHolder(
        private val binding: ItemCommunityPostpopularBinding)
        :ViewHolder(binding.root) {
        override fun bind(item: CommunityListItem) = with(binding){
            val popularItem: CommunityListItem.CommunityPopularItem = item as CommunityListItem.CommunityPopularItem

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
    ): ViewHolder(binding.root){
        override fun bind(item: CommunityListItem) = with(binding) {
            val writingItem: CommunityListItem.CommunityWritingItem = item as CommunityListItem.CommunityWritingItem

            writingItem.postInfo.let{
                tvItemWritingTitle.text = it.title
            }
        }

    }

    override fun getItemViewType(position: Int): Int = when(getItem(position)){
        is CommunityListItem.CommunityPopularItem -> PostItemViewType.POPULAR.ordinal
        is CommunityListItem.CommunityWritingItem -> PostItemViewType.POST.ordinal
        is CommunityListItem.CommunityCategoryItem -> PostItemViewType.CATEGORY.ordinal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityListAdapter.ViewHolder = when(viewType){
        PostItemViewType.POPULAR.ordinal -> {}
        PostItemViewType.POST.ordinal -> {}
        PostItemViewType.CATEGORY.ordinal -> {}
    }

    override fun onBindViewHolder(holder: CommunityListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}