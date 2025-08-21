package com.agvber.kekkek.presentation.smokingaddictiontest.assessment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.agvber.kekkek.presentation.smokingaddictiontest.model.SmokingAssessment

class SmokingAddictionTestAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val smokingAssessment = SmokingAssessment()

    private val fragments: List<Fragment> = (0..smokingAssessment.title.lastIndex).map { index ->
        SmokingAddictionTestViewPagerFragment.newInstance(
            title = fragment.getString(smokingAssessment.title[index]),
            question = smokingAssessment.answer[index].map { fragment.getString(it) }
                .toTypedArray(),
            pageIndex = index
        )
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(pageNum: Int): Fragment {
        return fragments[pageNum]
    }
}