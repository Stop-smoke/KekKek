package com.stopsmoke.kekkek.presentation.rankingList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.stopsmoke.kekkek.databinding.RecyclerviewRankinglistRankstateItemBinding
import com.stopsmoke.kekkek.domain.model.DateTime
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

    class ViewHolder(
        private val binding: RecyclerviewRankinglistRankstateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RankingListItem) = with(binding) {
            tvRankStateItem.text = item.name
            tvRankStateItemUserName.text = item.name
            tvRankStateItemRankNum.text = item.rank.toString()

            circleIvRankStateItemProfile.load(item.profile) {
//                    placeholder(R.drawable.placeholder_image)
//                    error(R.drawable.error_image)
                crossfade(true) // 이미지 로딩 시 페이드 인/아웃 효과 사용
                transformations(CircleCropTransformation()) // 원형 이미지로 잘라내기
            }

            tvRankingListTime.text = getRankingTime(item.time)
        }

        private fun getRankingTime(pastTime: LocalDateTime): String { //-일 -시간
            val currentTime = LocalDateTime.now()
            val duration = Duration.between(pastTime, currentTime)

            val days = duration.toDays()
            val hours = duration.toHours() % 24


            return "${days}일 ${hours}시간"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankingListAdapter.ViewHolder {
        return ViewHolder(
            RecyclerviewRankinglistRankstateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RankingListAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}