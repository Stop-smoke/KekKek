package com.stopsmoke.kekkek.presentation.home.center

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToHomeCenterScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.home_center, args = null,  navOptions = navOptions)
}