package com.stopsmoke.kekkek.presentation.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TestAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    callback: () -> Unit
) : FragmentStateAdapter(fragmentManager, lifecycle) {

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