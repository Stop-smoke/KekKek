package com.agvber.kekkek.presentation.my.bookmark

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyBookmarkScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_bookmark_list, args = null, navOptions = navOptions)
}