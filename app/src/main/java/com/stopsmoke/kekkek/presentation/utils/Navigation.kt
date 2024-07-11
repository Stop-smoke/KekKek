package com.stopsmoke.kekkek.presentation.utils

import androidx.navigation.NavOptions
import com.stopsmoke.kekkek.R

internal val defaultNavigationOption: NavOptions =
    NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_from_right_fade_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.slide_in_from_left_fade_in)
        .setPopExitAnim(R.anim.fade_out)
        .build()