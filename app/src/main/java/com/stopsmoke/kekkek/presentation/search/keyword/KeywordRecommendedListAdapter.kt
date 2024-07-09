package com.stopsmoke.kekkek.presentation.search.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewSearchRecommendBinding
import com.stopsmoke.kekkek.core.domain.model.RecommendedKeyword

class KeywordRecommendedListAdapter(
    private val keywordSelectedListener: (String) -> Unit
) : ListAdapter<RecommendedKeyword, KeywordRecommendedListAdapter.KeywordRecommendedViewHolder>(diffUtil) {

    class KeywordRecommendedViewHolder(
        private val binding: RecyclerviewSearchRecommendBinding,
        private val keywordSelectedListener: (String) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendedKeyword: RecommendedKeyword) {
            binding.tvSearchRecommend.text = recommendedKeyword.keyword

            binding.tvSearchRecommend.setOnClickListener {
                keywordSelectedListener(recommendedKeyword.keyword)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KeywordRecommendedViewHolder {
        val view =
            RecyclerviewSearchRecommendBinding.inflate(
                /* inflater = */ LayoutInflater.from(parent.context),
                /* parent = */ parent,
                /* attachToParent = */ false
        )
        return KeywordRecommendedViewHolder(view, keywordSelectedListener)
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