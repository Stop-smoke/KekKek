package com.agvber.kekkek.presentation.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToOnboardingGraph(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.nav_onboarding, args = null, navOptions = navOptions)
}