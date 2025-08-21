package com.agvber.kekkek.presentation.my.complaint

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyComplaintScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_complaint, args = null, navOptions = navOptions)
}