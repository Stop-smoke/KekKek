package com.stopsmoke.kekkek.presentation.post.detail

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption

private const val POST_ID_ARGUMENT = "post_id"

internal fun NavController.navigateToPostDetailScreen(
    postId: String,
    navOptions: NavOptions = defaultNavigationOption
) {
    val bundle = bundleOf(POST_ID_ARGUMENT to postId)
    navigate(resId = R.id.post_detail_screen, args = bundle,  navOptions = navOptions)
}