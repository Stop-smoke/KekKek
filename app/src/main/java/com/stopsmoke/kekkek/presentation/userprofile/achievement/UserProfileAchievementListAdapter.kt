package com.stopsmoke.kekkek.presentation.userprofile.achievement

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.databinding.RecyclerviewAchievementItemBinding
import com.stopsmoke.kekkek.presentation.my.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.utils.diffutil.AchievementItemDiffUtil

class UserProfileAchievementListAdapter :
    ListAdapter<AchievementItem, UserProfileAchievementListAdapter.AchievementViewHolder>(
        AchievementItemDiffUtil()
    ) {

    class AchievementViewHolder(
        val binding: RecyclerviewAchievementItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(achievement: AchievementItem) = with(binding) {

            tvAchievementTitle.text = achievement.name

            val progressPercentage = (achievement.progress.toDouble() * 100).toInt()
            liAchievementProgress.apply {
                setProgress(0, false)

                post {
                    val targetProgress = if (progressPercentage > 100) 100 else progressPercentage
                    setProgress(targetProgress, true)
                }
            }

            tvAchievementDescription.text = achievement.description

            var textCurrentProgress = 0

            if (achievement.currentProgress >= achievement.maxProgress
                && achievement.category != DatabaseCategory.RANK
            ) { // 랭킹 이외 업적 클리어
                textCurrentProgress = achievement.maxProgress
                clAchievementRoot.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.gray_achievement_clear
                    )
                )
                ivAchievementItemChecked.visibility = View.VISIBLE
            } else if (achievement.currentProgress < achievement.maxProgress // 랭킹 이외 업적 논 클리어
                && achievement.category != DatabaseCategory.RANK
            ) {
                textCurrentProgress = achievement.currentProgress
                clAchievementRoot.setBackgroundColor(Color.WHITE)
                ivAchievementItemChecked.visibility = View.GONE
            } else if (achievement.currentProgress <= achievement.maxProgress // 랭킹 업적 클리어
                && achievement.category == DatabaseCategory.RANK
            ) {
                textCurrentProgress = achievement.maxProgress
                clAchievementRoot.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.gray_achievement_clear
                    )
                )
                ivAchievementItemChecked.visibility = View.VISIBLE
            } else if (achievement.currentProgress > achievement.maxProgress // 랭킹 업적 논 클리어
                && achievement.category == DatabaseCategory.RANK
            ) {
                textCurrentProgress = achievement.currentProgress
                clAchievementRoot.setBackgroundColor(Color.WHITE)
                ivAchievementItemChecked.visibility = View.GONE
            }

            tvAchievementProgressNumber.text = "${textCurrentProgress} / ${achievement.maxProgress}"

            civAchievementImage.load(achievement.image)

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
}