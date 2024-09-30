package com.agvber.kekkek.presentation.reply

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.agvber.kekkek.R
import com.agvber.kekkek.presentation.utils.defaultNavigationOption

private const val POST_ID_ARGUMENT = "post_id"
private const val COMMENT_ID_ARGUMENT = "comment_id"

internal fun NavController.navigateToReplyScreen(
    postId: String,
    commentId: String,
    navOptions: NavOptions = defaultNavigationOption
) {
    val bundle = bundleOf(POST_ID_ARGUMENT to postId, COMMENT_ID_ARGUMENT to commentId)
    navigate(resId = R.id.reply, args = bundle, navOptions = navOptions)
}