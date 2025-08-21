package com.agvber.kekkek.presentation.post.notice

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToPostNoticeScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.notice_list, args = null,  navOptions = navOptions)
}