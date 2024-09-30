package com.agvber.kekkek.presentation.community

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.R
import com.agvber.kekkek.databinding.ItemRecyclerviewCommunityCategoryBinding
import com.agvber.kekkek.presentation.utils.diffutil.StringDiffUtil

class CommunityCategoryListAdapter(
    private val onClick: (clickPosition: Int) -> Unit
): ListAdapter<String, CommunityCategoryListAdapter.ViewHolder>(StringDiffUtil()) {
    private val items = mutableListOf<TextView>()
    private var selectPosition = 0

    class ViewHolder(
        private val binding: ItemRecyclerviewCommunityCategoryBinding,
        private val adapter: CommunityCategoryListAdapter,
        private val onClick: (clickPosition: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) = with(binding) {
            adapter.items.add(tvItemCommunityRvCategoryCategory)
            tvItemCommunityRvCategoryCategory.text = item
            if (adapterPosition == adapter.selectPosition) tvItemCommunityRvCategoryCategory.setBackgroundResource(R.drawable.bg_community_categorybox_blue)
            else {
                tvItemCommunityRvCategoryCategory.setBackgroundResource(R.drawable.bg_community_categorybox_gray)
            }

            tvItemCommunityRvCategoryCategory.setOnClickListener {
                onClick(adapterPosition)
                adapter.items.forEachIndexed { index, _ ->
                    adapter.notifyItemChanged(index)
                    adapter.selectPosition = adapterPosition
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemRecyclerviewCommunityCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ,this
            ,onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getSelectPosition() = selectPosition
}