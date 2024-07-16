package com.stopsmoke.kekkek.presentation.onboarding

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToOnboardingGraph(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.nav_onboarding, args = null, navOptions = navOptions)
}