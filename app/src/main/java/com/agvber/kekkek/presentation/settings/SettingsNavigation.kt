package com.agvber.kekkek.presentation.settings

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSettingsGraph(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.nav_settings, args = null, navOptions = navOptions)
}