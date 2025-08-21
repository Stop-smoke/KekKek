package com.agvber.kekkek.presentation.my.comment

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyCommentScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_comment_screen, args = null, navOptions = navOptions)
}