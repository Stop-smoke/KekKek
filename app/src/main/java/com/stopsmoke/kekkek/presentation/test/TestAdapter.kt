package com.stopsmoke.kekkek.presentation.test

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TestAdapter(
    fragment: Fragment,
    callback: () -> Unit
) : FragmentStateAdapter(fragment) {

    private val fragments: List<Fragment> = (0..9).map {
        if (it == 0) {
            return@map TestOnBoardingFragment(callback)
        }
        if (it == 9) {
            return@map TestResultFragment()
        }
        TestQuestionFragment.newInstance(it-1)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(pageNum: Int): Fragment {
        return fragments[pageNum]
    }
}