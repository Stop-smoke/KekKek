package com.stopsmoke.kekkek.presentation.my.complaint

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

internal fun NavController.navigateToMyComplaintScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.my_complaint, args = null, navOptions = navOptions)
}