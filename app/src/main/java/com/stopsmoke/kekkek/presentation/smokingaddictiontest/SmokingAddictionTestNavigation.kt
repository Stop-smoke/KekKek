package com.stopsmoke.kekkek.presentation.smokingaddictiontest

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSmokingAddictionTestGraph(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.nav_smoking_addiction_test, args = null,  navOptions = navOptions)
}

internal fun NavController.navigateToSmokingAddictionTestScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
   navigate(resId = R.id.smoking_addiction_test_screen, args = null, navOptions = navOptions)
}

internal fun NavController.navigateToSmokingAddictionTestResultScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.smoking_addiction_test_result_screen, args = null, navOptions = navOptions)
}