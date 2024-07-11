package com.stopsmoke.kekkek.presentation.ranking

import androidx.fragment.app.Fragment

interface RankingListCallback {
    fun navigationToUserProfile(uid: String)

    fun replaceFragment(fragment: Fragment)
}