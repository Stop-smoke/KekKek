package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.presentation.home.rankingList.RankingListItem

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