package com.stopsmoke.kekkek.presentation.community

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostToPostViewItem (
    val postId:String,
    val position: Int
) : Parcelable