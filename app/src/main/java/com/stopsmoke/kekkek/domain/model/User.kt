package com.stopsmoke.kekkek.domain.model

import com.stopsmoke.kekkek.firestore.model.LocationEntity

data class User(
    val uid: String,
    val name: String,
    val location: Location?,
    var profileImage: ProfileImage,

    val nickname: String? = null,
    val gender: String? = null,
    val age: Int? = null,
    var phoneNumber: String? = null,
    var fcmToken: String? = null
)