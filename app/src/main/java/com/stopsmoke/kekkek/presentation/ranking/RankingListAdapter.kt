package com.stopsmoke.kekkek.presentation.ranking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.RecyclerviewRankinglistRankstateItemBinding
import com.stopsmoke.kekkek.presentation.ranking.field.RankingListField
import com.stopsmoke.kekkek.presentation.utils.diffutil.RankingListItemDiffUtil
import java.time.Duration
import java.time.LocalDateTime

class RankingListAdapter(
    private val rankingListField: RankingListField
) : ListAdapter<RankingListItem, RankingListAdapter.ViewHolder>(RankingListItemDiffUtil()) {

    private var callback: RankingListCallback? = null

    fun registerCallbackListener(callback: RankingListCallback) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }

    inner class ViewHolder(
        private val binding: RecyclerviewRankinglistRankstateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RankingListItem) = with(binding) {
            tvRankStateItem.text = item.introduction
            tvRankStateItemUserName.text = item.name
            tvRankStateItemRankNum.text = (absoluteAdapterPosition + 1).toString()

            if (item.profileImage.isNullOrEmpty()) {
                circleIvRankStateItemProfile.setImageResource(
                    R.drawable.img_defaultprofile
                )
            } else {
                circleIvRankStateItemProfile.load(item.profileImage)
            }

            tvRankingListTime.text = when(rankingListField) {
                RankingListField.Achievement -> item.clearAchievementInt.toString() + "개"
                RankingListField.Time -> getRankingTime(item.startTime!!)
            }
            circleIvRankStateItemProfile.setOnClickListener {
                callback?.navigationToUserProfile(item.userID)
            }
        }

        private fun getRankingTime(startTime: LocalDateTime): String { //-일 -시간
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(startTime, currentTime)

            val days = duration.toDays()
            val hours = duration.toHours() % 24


            return "${days}일 ${hours}시간"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            RecyclerviewRankinglistRankstateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}