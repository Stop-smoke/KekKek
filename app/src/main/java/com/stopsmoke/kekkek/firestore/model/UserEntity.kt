package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class UserEntity(
    @PropertyName("uid") val uid: String? = null,
    @PropertyName("name") val name: String? = null,
    @PropertyName("nickname") val nickname: String? = null,
    @PropertyName("gender") val gender: String? = null,
    @PropertyName("age") val age: Int? = null,
    @PropertyName("location") val location: LocationEntity? = null,
    @get:PropertyName("phone_number") @set:PropertyName("phone_number") var phoneNumber: String? = null,
    @get:PropertyName("profile_image_url") @set:PropertyName("profile_image_url") var profileImageUrl: String? = null,
)