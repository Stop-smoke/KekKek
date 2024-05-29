package com.stopsmoke.kekkek.presentation.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityPopularViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val itemList = listOf(
        CommunityPopularItem( "ic_launcher_background", "나는 문어"),
        CommunityPopularItem( "ic_launcher_background", "나는 문어"),
        CommunityPopularItem( "ic_launcher_background", "나는 문어"),
        CommunityPopularItem( "ic_launcher_background", "나는 문어"),
    )

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PopularHomeItemFragment.newInstance(itemList[position])
    }

}