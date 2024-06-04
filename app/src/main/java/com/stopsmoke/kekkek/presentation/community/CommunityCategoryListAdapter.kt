package com.stopsmoke.kekkek.presentation.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemRecyclerviewCommunityCategoryBinding

class CommunityCategoryListAdapter
    : ListAdapter<String, CommunityCategoryListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    class ViewHolder(
        private val binding: ItemRecyclerviewCommunityCategoryBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(item:String) = with(binding){
            tvItemCommunityRvCategoryCategory.text = item
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommunityCategoryListAdapter.ViewHolder {
        return ViewHolder(
            ItemRecyclerviewCommunityCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: CommunityCategoryListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}