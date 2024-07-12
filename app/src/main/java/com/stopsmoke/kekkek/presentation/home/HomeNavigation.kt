package com.stopsmoke.kekkek.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToHomeGraph(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.nav_graph, args = null, navOptions = navOptions)
}

internal fun NavController.popBackStackInclusiveHome() {
    popBackStack(R.id.home, false)
}