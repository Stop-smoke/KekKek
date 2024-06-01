package com.stopsmoke.kekkek

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    callback: () -> Unit
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments: List<Fragment> = (0..7).map {
        if (it == 0) {
            return@map TestOnBoardingFragment(callback)
        }
        if (it == 7) {
            return@map TestResultFragment()
        }
        QuestionFragment.newInstance(it)
    }


    override fun getItemCount(): Int = fragments.size

    override fun createFragment(pageNum: Int): Fragment {
        return fragments[pageNum]
    }
}