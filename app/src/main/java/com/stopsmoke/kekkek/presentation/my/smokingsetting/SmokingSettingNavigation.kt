package com.stopsmoke.kekkek.presentation.my.smokingsetting

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSmokingSettingScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.settings_smoking_setting, args = null, navOptions = navOptions)
}