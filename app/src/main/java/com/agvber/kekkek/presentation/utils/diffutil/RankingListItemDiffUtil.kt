package com.agvber.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.agvber.kekkek.presentation.ranking.RankingListItem

class RankingListItemDiffUtil : DiffUtil.ItemCallback<RankingListItem>() {

    override fun areItemsTheSame(
        oldItem: RankingListItem,
        newItem: RankingListItem
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: RankingListItem,
        newItem: RankingListItem
    ): Boolean {
        return oldItem === newItem
    }
}