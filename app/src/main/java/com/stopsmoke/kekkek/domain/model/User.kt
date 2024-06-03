package com.stopsmoke.kekkek.domain.model

data class User(
    val uid: String,
    val name: String,
    val location: Location?,
    var profileImage: ProfileImage,
)