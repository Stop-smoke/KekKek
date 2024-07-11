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

internal fun NavController.navigateToHomeGraphWithBackStackClear() {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_from_right_fade_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.slide_in_from_left_fade_in)
        .setPopExitAnim(R.anim.fade_out)
        .setPopUpTo(R.id.nav_graph, false)
        .build()

    navigate(resId = R.id.nav_graph, args = null, navOptions = navOptions)
}

internal fun NavController.popBackStackInclusiveHome() {
    popBackStack(R.id.home, false)
}