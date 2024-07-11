package com.stopsmoke.kekkek.presentation.post.notice

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToPostNoticeScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.notice_list, args = null,  navOptions = navOptions)
}