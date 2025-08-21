package com.agvber.kekkek.presentation.my.achievement

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToAchievementScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.achievement, args = null, navOptions = navOptions)
}