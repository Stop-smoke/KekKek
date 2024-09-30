package com.agvber.kekkek.presentation.my.smokingsetting

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSmokingSettingScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.settings_smoking_setting, args = null, navOptions = navOptions)
}