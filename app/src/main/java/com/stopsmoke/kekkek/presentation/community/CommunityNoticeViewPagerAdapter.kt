package com.stopsmoke.kekkek.presentation.community

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CommunityNoticeViewPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    private val itemList = listOf(
        listOf(
            "나는 문어~~ 꿈을 꾸는 문어~~",
            "나는 낭만고양이~~~~ 도시를 비춰~~~~ 나는 낭만~~",
            "오늘밤 바라본~~ 저 달이 너무 처량해~~~"
        ),
        listOf(
            "나는 문어~~ 꿈을 꾸는 문어~~",
            "나는 낭만고양이~~~~ 도시를 비춰~~~~ 나는 낭만~~",
            "오늘밤 바라본~~ 저 달이 너무 처량해~~~"
        ),
        listOf(
            "나는 문어~~ 꿈을 꾸는 문어~~",
            "나는 낭만고양이~~~~ 도시를 비춰~~~~ 나는 낭만~~",
            "오늘밤 바라본~~ 저 달이 너무 처량해~~~"
        )
    )

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun createFragment(position: Int): Fragment {
        return NoticeHomeItemFragment.newInstance(itemList[position])
    }

}