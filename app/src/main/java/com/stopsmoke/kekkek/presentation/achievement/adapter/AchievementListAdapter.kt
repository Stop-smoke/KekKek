package com.stopsmoke.kekkek.presentation.achievement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewAchievementItemBinding
import com.stopsmoke.kekkek.domain.model.Achievement

class AchievementListAdapter :
    ListAdapter<Achievement, AchievementListAdapter.AchievementViewHolder>(diffUtil) {

    class AchievementViewHolder(
        val binding: RecyclerviewAchievementItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(achievement: Achievement) {
            binding.tvAchievementTitle.text = achievement.title
            binding.liAchievementProgress.progress = achievement.currentProgress
            binding.liAchievementProgress.max = achievement.maxProgress
            binding.tvAchievementDescription.text = achievement.description
            binding.tvAchievementProgressNumber.text =
                "${achievement.currentProgress}/${achievement.maxProgress}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = RecyclerviewAchievementItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<Achievement>() {
            override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean {
                return oldItem == newItem
            }

        }
    }
}