package com.agvber.kekkek.presentation.post.popular

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToPostPopularScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.popular_post_screen, args = null,  navOptions = navOptions)
}