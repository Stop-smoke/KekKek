package com.agvber.kekkek.presentation.notification

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToNotificationScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.notification, args = null, navOptions = navOptions)
}