package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.Timestamp
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
    @get:PropertyName("fcm_token") @set:PropertyName("fcm_token")
    var fcmToken: String? = null,
    @get:PropertyName("introduction") @set:PropertyName("introduction") var introduction: String? = null,
    @get:PropertyName("ranking") @set:PropertyName("ranking") var ranking: Int? = null,
    @get:PropertyName("post_bookmark") @set:PropertyName("post_bookmark") var post_bookmark: List<String>? = null,
    @get:PropertyName("post_like") @set:PropertyName("post_like") var post_like: List<String>? = null,
    @get:PropertyName("start_time") @set:PropertyName("start_time") var start_time: Timestamp? = null,
)