package com.stopsmoke.kekkek.presentation.authentication

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption
import com.stopsmoke.kekkek.presentation.utils.newBuilder

internal fun NavController.navigateToAuthenticationScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.authentication, args = null, navOptions = navOptions)
}

internal fun NavController.navigateToAuthenticationScreenWithBackStackClear(
    navOptions: NavOptions = defaultNavigationOption
) {
    val newNavOption = navOptions.newBuilder()
        .setPopUpTo(destinationId = R.id.nav_graph, inclusive = true)
        .build()

    navigate(resId = R.id.authentication, args = null, navOptions = newNavOption)
}