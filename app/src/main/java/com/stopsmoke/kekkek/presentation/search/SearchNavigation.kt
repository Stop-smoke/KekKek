package com.stopsmoke.kekkek.presentation.search

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToSearchScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.search, args = null,  navOptions = navOptions)
}