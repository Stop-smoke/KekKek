package com.stopsmoke.kekkek.presentation.userprofile

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

private const val UID_ARGUMENT = "uid"

internal fun NavController.navigateToUserProfileScreen(
    uid: String,
    navOptions: NavOptions = defaultNavigationOption
) {
    val bundle = bundleOf(UID_ARGUMENT to uid)
    navigate(resId = R.id.user_profile, args = bundle,  navOptions = navOptions)
}