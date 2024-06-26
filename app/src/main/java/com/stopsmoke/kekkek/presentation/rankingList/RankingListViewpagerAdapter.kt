package com.stopsmoke.kekkek.presentation.rankingList

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stopsmoke.kekkek.presentation.userprofile.achievement.UserProfileAchievementFragment
import com.stopsmoke.kekkek.presentation.userprofile.comment.UserProfileCommentFragment
import com.stopsmoke.kekkek.presentation.userprofile.post.UserProfilePostFragment

class RankingListViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = listOf(
        RegionalTopRankFragment.newInstance("전국"),
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}