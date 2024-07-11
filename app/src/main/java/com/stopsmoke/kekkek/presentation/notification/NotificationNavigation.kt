package com.stopsmoke.kekkek.presentation.notification

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToNotificationScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.notification, args = null, navOptions = navOptions)
}