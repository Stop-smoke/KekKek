package com.stopsmoke.kekkek.presentation.achievement.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.databinding.RecyclerviewAchievementItemBinding
import com.stopsmoke.kekkek.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.achievement.AchievementViewModel

class AchievementListAdapter(
) :
    ListAdapter<AchievementItem, AchievementListAdapter.AchievementViewHolder>(diffUtil) {

    class AchievementViewHolder(
        val binding: RecyclerviewAchievementItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: AchievementItem) {

            binding.tvAchievementTitle.text = achievement.name

            val progressPercentage = (achievement.progress.toDouble() * 100).toInt()
            binding.liAchievementProgress.apply {
                setProgress(0, false)

                post {
                    val targetProgress = if (progressPercentage > 100) 100 else progressPercentage
                    setProgress(targetProgress, true)
                }
            }

            binding.tvAchievementDescription.text = achievement.description

            var textCurrentProgress = 0
            if(achievement.currentProgress >= achievement.maxProgress
                && achievement.category != DatabaseCategory.RANK) {
                textCurrentProgress = achievement.maxProgress
                setRainbow(binding.clAchievementRoot)
            }
            else {
                textCurrentProgress = achievement.currentProgress
                binding.clAchievementRoot.setBackgroundColor(Color.WHITE)
            }
            binding.tvAchievementProgressNumber.text = "${textCurrentProgress} / ${achievement.maxProgress}"

            binding.civAchievementImage.load(achievement.image)
        }

        private fun setRainbow(cl: ConstraintLayout){
            val colors = intArrayOf(
                Color.RED,
                Color.YELLOW,
                Color.GREEN,
                Color.CYAN,
                Color.BLUE,
                Color.MAGENTA
            )

            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
            gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            gradientDrawable.cornerRadius = 0f

            cl.background = gradientDrawable
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
        private val diffUtil = object : DiffUtil.ItemCallback<AchievementItem>() {
            override fun areItemsTheSame(
                oldItem: AchievementItem,
                newItem: AchievementItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: AchievementItem,
                newItem: AchievementItem
            ): Boolean {
                return oldItem==newItem
            }

        }
    }
}