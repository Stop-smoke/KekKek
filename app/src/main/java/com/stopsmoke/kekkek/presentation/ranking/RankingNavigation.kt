package com.stopsmoke.kekkek.presentation.ranking

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToRankingScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.ranking_list, args = null, navOptions = navOptions)
}