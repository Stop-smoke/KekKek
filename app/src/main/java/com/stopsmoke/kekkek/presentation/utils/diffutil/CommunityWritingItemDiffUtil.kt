package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem

class CommunityWritingItemDiffUtil : DiffUtil.ItemCallback<CommunityWritingItem>() {

    override fun areItemsTheSame(
        oldItem: CommunityWritingItem,
        newItem: CommunityWritingItem,
    ): Boolean {
        return oldItem.postInfo.id == newItem.postInfo.id
    }

    override fun areContentsTheSame(
        oldItem: CommunityWritingItem,
        newItem: CommunityWritingItem,
    ): Boolean {
        return oldItem === newItem
    }
}