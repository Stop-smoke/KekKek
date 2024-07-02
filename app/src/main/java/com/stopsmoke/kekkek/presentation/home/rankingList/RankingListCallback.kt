package com.stopsmoke.kekkek.presentation.home.rankingList

import androidx.fragment.app.Fragment

interface RankingListCallback {
    fun navigationToUserProfile(uid: String)

    fun replaceFragment(fragment: Fragment)
}