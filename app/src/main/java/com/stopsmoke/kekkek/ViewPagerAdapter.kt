package com.stopsmoke.kekkek

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 8 // 리턴 개수: 만드는 질문지의 총 개수
    }

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(position)
    }
}