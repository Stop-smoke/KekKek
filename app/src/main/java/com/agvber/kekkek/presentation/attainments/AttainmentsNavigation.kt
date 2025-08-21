package com.agvber.kekkek.presentation.attainments

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToAttainmentsScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.attainments, args = null,  navOptions = navOptions)
}