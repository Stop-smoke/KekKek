package com.stopsmoke.kekkek.presentation.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostWriteItem(
    val postType: String,
    val title: String,
    val content: String
): Parcelable
