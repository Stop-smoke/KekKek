package com.stopsmoke.kekkek.presentation.achievement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewAchievementItemBinding
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.presentation.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.achievement.AchievementViewModel

class AchievementListAdapter(
    private val viewModel: AchievementViewModel
) :
    ListAdapter<AchievementItem, AchievementListAdapter.AchievementViewHolder>(diffUtil) {

    class AchievementViewHolder(
        val binding: RecyclerviewAchievementItemBinding,
        private val viewModel: AchievementViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(achievement: AchievementItem) {
            val currentProgress: Int = when (achievement.category) {
                DatabaseCategory.COMMENT -> viewModel.currentProgressItem.comment
                DatabaseCategory.POST ->  viewModel.currentProgressItem.post
                DatabaseCategory.USER ->  viewModel.currentProgressItem.time
                DatabaseCategory.ACHIEVEMENT ->  viewModel.currentProgressItem.achievement
                DatabaseCategory.RANK ->  viewModel.currentProgressItem.rank
                DatabaseCategory.ALL -> 0
            }

            binding.tvAchievementTitle.text = achievement.name
            binding.liAchievementProgress.progress = currentProgress
            binding.liAchievementProgress.max = achievement.maxProgress
            binding.tvAchievementDescription.text = achievement.content
            binding.tvAchievementProgressNumber.text =
                "${currentProgress}/${achievement.maxProgress}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = RecyclerviewAchievementItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AchievementViewHolder(view, viewModel)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<AchievementItem>() {
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
                return oldItem == newItem
            }

        }
    }
}