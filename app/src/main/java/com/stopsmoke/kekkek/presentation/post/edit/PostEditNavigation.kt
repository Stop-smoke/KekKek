package com.stopsmoke.kekkek.presentation.post.edit

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

private const val POST_ID_ARGUMENT = "post_id"

internal fun NavController.navigateToPostEditScreen(
    navOptions: NavOptions = defaultNavigationOption
) {
    navigate(resId = R.id.post_write, args = null,  navOptions = navOptions)
}

internal fun NavController.navigateToPostEditScreen(
    postId: String,
    navOptions: NavOptions = defaultNavigationOption
) {
    val bundle = bundleOf(POST_ID_ARGUMENT to postId)
    navigate(resId = R.id.post_write, args = bundle,  navOptions = navOptions)
}