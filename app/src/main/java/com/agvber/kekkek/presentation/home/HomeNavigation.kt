package com.agvber.kekkek.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption
import com.agvber.kekkek.presentation.utils.newBuilder

internal fun NavController.navigateToHomeScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.home, args = null, navOptions = navOptions)
}

internal fun NavController.popBackStackInclusiveHome() {
    popBackStack(R.id.home, false)
}

internal fun NavController.navigateToHomeScreenWithClearBackStack(
    navOptions: NavOptions = defaultNavigationOption
) {
    val newNavOption = navOptions.newBuilder()
        .setPopUpTo(destinationId = R.id.nav_graph, inclusive = true)
        .build()
    navigate(resId = R.id.home, args = null, navOptions = newNavOption)
}
