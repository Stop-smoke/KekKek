package com.agvber.kekkek.presentation.my.post

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyPostScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_post_screen, args = null, navOptions = navOptions)
}