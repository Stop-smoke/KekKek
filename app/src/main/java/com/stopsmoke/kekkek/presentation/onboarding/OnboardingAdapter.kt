package com.stopsmoke.kekkek.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3 // 온보딩 화면 개수

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingIntroduceFragment()
            else -> OnboardingIntroduceFragment()
        }
    }
}