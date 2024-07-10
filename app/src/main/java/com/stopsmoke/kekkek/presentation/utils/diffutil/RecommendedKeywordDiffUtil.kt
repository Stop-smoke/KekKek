package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.core.domain.model.RecommendedKeyword

class RecommendedKeywordDiffUtil : DiffUtil.ItemCallback<RecommendedKeyword>() {
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
        return oldItem === newItem
    }

}