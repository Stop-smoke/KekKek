package com.stopsmoke.kekkek.presentation.rankingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.RecyclerviewRankinglistRankstateItemBinding
import com.stopsmoke.kekkek.domain.model.HistoryTime
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.Ranking
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import java.time.Duration
import java.time.LocalDateTime

class RankingListAdapter
    : ListAdapter<RankingListItem, RankingListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<RankingListItem>() {
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
            return oldItem == newItem
        }
    }
) {
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
            tvRankStateItem.text = item.name
            tvRankStateItemUserName.text = item.name
            tvRankStateItemRankNum.text = (absoluteAdapterPosition + 1).toString()

            if (item.profileImage.isNullOrEmpty()) {
                circleIvRankStateItemProfile.setImageResource(
                    R.drawable.img_defaultprofile
                )
            } else {
                circleIvRankStateItemProfile.load(item.profileImage)
            }

            tvRankingListTime.text = getRankingTime(item.startTime!!)

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