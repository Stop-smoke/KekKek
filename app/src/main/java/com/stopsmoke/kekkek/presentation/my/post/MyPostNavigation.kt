package com.stopsmoke.kekkek.presentation.my.post

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyPostScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_post_screen, args = null, navOptions = navOptions)
}