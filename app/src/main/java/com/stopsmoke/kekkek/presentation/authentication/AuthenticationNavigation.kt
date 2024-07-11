package com.stopsmoke.kekkek.presentation.authentication

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToAuthenticationScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.authentication, args = null, navOptions = navOptions)
}

internal fun NavController.navigateToAuthenticationScreenWithBackStackClear() {

    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_from_right_fade_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.slide_in_from_left_fade_in)
        .setPopExitAnim(R.anim.fade_out)
        .setPopUpTo(R.id.authentication, false)
        .build()

    navigate(resId = R.id.authentication, args = null, navOptions = navOptions)
}