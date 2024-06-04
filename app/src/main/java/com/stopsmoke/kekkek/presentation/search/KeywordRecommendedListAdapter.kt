package com.stopsmoke.kekkek.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.stopsmoke.kekkek.databinding.RecyclerviewSearchRecommendBinding
import com.stopsmoke.kekkek.domain.model.RecommendedKeyword

class KeywordRecommendedListAdapter :
    ListAdapter<RecommendedKeyword, KeywordRecommendedViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeywordRecommendedViewHolder {
        val view = RecyclerviewSearchRecommendBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return KeywordRecommendedViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeywordRecommendedViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<RecommendedKeyword>() {
            override fun areItemsTheSame(
                oldItem: RecommendedKeyword,
                newItem: RecommendedKeyword,
            ): Boolean {
                return oldItem.keyword == newItem.keyword
            }

            override fun areContentsTheSame(
                oldItem: RecommendedKeyword,
                newItem: RecommendedKeyword,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}