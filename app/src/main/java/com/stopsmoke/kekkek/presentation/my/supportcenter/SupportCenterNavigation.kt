package com.stopsmoke.kekkek.presentation.my.supportcenter

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSupportCenterScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_supportcenter, args = null, navOptions = navOptions)
}