package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.presentation.my.achievement.AchievementItem

class AchievementItemDiffUtil : DiffUtil.ItemCallback<AchievementItem>() {

    override fun areItemsTheSame(
        oldItem: AchievementItem,
        newItem: AchievementItem
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: AchievementItem,
        newItem: AchievementItem
    ): Boolean {
        return oldItem === newItem
    }
}