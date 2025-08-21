package com.agvber.kekkek.presentation.home.tip

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToHomeTipScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.home_tip, args = null,  navOptions = navOptions)
}