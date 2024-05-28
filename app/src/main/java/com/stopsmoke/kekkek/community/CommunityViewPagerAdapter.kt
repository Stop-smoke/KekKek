package com.stopsmoke.kekkek.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val imageList = listOf(
        "ic_launcher_background",
        "ic_launcher_background",
        "ic_launcher_background",
        "ic_launcher_background",
    )

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(imageList[position])
    }

}