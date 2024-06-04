package com.stopsmoke.kekkek.presentation.search

import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewSearchRecommendBinding
import com.stopsmoke.kekkek.domain.model.RecommendedKeyword

class KeywordRecommendedViewHolder(
    val binding: RecyclerviewSearchRecommendBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(recommendedKeyword: RecommendedKeyword) {
        binding.tvSearchRecommend.text = recommendedKeyword.keyword
    }
}