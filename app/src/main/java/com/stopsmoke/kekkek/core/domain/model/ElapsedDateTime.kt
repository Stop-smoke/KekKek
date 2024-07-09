package com.stopsmoke.kekkek.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ElapsedDateTime(
    val elapsedDateTime: DateTimeUnit,
    val number: Int,
) : Parcelable