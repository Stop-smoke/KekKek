package com.stopsmoke.kekkek.presentation.userprofile.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.stopsmoke.kekkek.presentation.userprofile.achievement.UserProfileAchievementFragment
import com.stopsmoke.kekkek.presentation.userprofile.comment.UserProfileCommentFragment
import com.stopsmoke.kekkek.presentation.userprofile.post.UserProfilePostFragment

class UserProfileViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        UserProfilePostFragment(),
        UserProfileCommentFragment(),
        UserProfileAchievementFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}