package com.stopsmoke.kekkek.presentation.attainments

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToAttainmentsScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.attainments, args = null,  navOptions = navOptions)
}