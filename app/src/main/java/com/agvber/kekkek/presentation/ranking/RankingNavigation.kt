package com.agvber.kekkek.presentation.ranking

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToRankingScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.ranking_list, args = null, navOptions = navOptions)
}