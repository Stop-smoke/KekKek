package com.agvber.kekkek.presentation.search

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSearchScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.search, args = null,  navOptions = navOptions)
}