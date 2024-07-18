package com.stopsmoke.kekkek.presentation.settings

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSettingsGraph(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.nav_settings, args = null, navOptions = navOptions)
}