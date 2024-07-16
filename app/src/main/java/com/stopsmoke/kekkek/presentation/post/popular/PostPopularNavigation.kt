package com.stopsmoke.kekkek.presentation.post.popular

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToPostPopularScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.popular_post_screen, args = null,  navOptions = navOptions)
}