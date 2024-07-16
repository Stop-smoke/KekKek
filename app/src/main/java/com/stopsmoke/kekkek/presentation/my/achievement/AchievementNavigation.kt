package com.stopsmoke.kekkek.presentation.my.achievement

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToAchievementScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.achievement, args = null, navOptions = navOptions)
}