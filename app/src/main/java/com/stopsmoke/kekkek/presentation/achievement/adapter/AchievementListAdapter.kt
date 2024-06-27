package com.stopsmoke.kekkek.presentation.achievement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewAchievementItemBinding
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.achievement.AchievementViewModel

class AchievementListAdapter(
    private val viewModel: AchievementViewModel
) :
    PagingDataAdapter<AchievementItem, AchievementListAdapter.AchievementViewHolder>(diffUtil) {

    class AchievementViewHolder(
        val binding: RecyclerviewAchievementItemBinding,
        private val viewModel: AchievementViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        val clearList = when (viewModel.user.value) {
            is User.Registered -> (viewModel.user.value as User.Registered).clearAchievementsList
            else -> emptyList()
        }

        fun bind(achievement: AchievementItem) {
            val currentProgressItem = viewModel.getCurrentItem()

            val currentProgress: Long = when (achievement.category) {
                DatabaseCategory.COMMENT -> currentProgressItem.comment
                DatabaseCategory.POST -> currentProgressItem.post
                DatabaseCategory.USER -> currentProgressItem.user
                DatabaseCategory.ACHIEVEMENT -> currentProgressItem.achievement
                DatabaseCategory.RANK -> currentProgressItem.rank
                DatabaseCategory.ALL -> 0
            }

            binding.tvAchievementTitle.text = achievement.name
            binding.liAchievementProgress.progress = currentProgress.toInt()
            binding.liAchievementProgress.max = achievement.maxProgress
            binding.tvAchievementDescription.text = achievement.description
            if (currentProgress < achievement.maxProgress) {
                binding.tvAchievementProgressNumber.text =
                    "${currentProgress}/${achievement.maxProgress}"
            } else {
                binding.tvAchievementProgressNumber.text =
                    "${achievement.maxProgress}/${achievement.maxProgress}"

                if (achievement.id !in clearList) {
                    viewModel.upDateUserAchievementList(achievement.id)
                }
            }
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