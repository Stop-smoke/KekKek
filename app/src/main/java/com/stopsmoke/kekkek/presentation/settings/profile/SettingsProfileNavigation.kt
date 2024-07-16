package com.stopsmoke.kekkek.presentation.settings.profile

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSettingsProfileScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.settings_setting_profile, args = null, navOptions = navOptions)
}