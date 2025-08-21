package com.agvber.kekkek.presentation.utils

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun ImageView.handleImageColor(
    context: Context,
    @ColorRes activeColor: Int,
    @ColorRes passiveColor: Int,
    isActive: Boolean
) {
    if (isActive) {
        setColorFilter(ContextCompat.getColor(context, activeColor))
        return
    }
    setColorFilter(ContextCompat.getColor(context, passiveColor))
}