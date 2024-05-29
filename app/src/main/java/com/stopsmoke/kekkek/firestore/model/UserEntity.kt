package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class UserEntity(
    @PropertyName("id") val id: String? = null,
    @PropertyName("name") val name: String? = null,
    @PropertyName("nickname") val nickname: String? = null,
    @PropertyName("gender") val gender: String? = null,
    @PropertyName("age") val age: Int? = null,
    @PropertyName("location") val location: LocationEntity? = null,
    @get:PropertyName("phone_number") val phoneNumber: String? = null,
    @get:PropertyName("profile_image_url") val profileImageUrl: String? = null,
)