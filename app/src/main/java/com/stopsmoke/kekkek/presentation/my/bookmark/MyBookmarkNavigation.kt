package com.stopsmoke.kekkek.presentation.my.bookmark

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyBookmarkScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_bookmark_list, args = null, navOptions = navOptions)
}