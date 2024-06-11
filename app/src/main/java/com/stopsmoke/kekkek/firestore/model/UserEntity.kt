package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class UserEntity(
    @PropertyName("uid")
    val uid: String? = null,

    @PropertyName("name")
    val name: String? = null,

    @PropertyName("nickname")
    val nickname: String? = null,

    @PropertyName("gender")
    val gender: String? = null,

    @PropertyName("age")
    val age: Int? = null,

    @PropertyName("location")
    val location: LocationEntity? = null,

    @get:PropertyName("phone_number") @set:PropertyName("phone_number")
    var phoneNumber: String? = null,

    @get:PropertyName("profile_image_url") @set:PropertyName("profile_image_url")
    var profileImageUrl: String? = null,

    @get:PropertyName("fcm_token") @set:PropertyName("fcm_token")
    var fcmToken: String? = null,

    @get:PropertyName("introduction") @set:PropertyName("introduction")
    var introduction: String? = null,

    @get:PropertyName("ranking") @set:PropertyName("ranking")
    var ranking: Long? = null,

    @get:PropertyName("post_bookmark") @set:PropertyName("post_bookmark")
    var postBookmark: List<String>? = null,

    @get:PropertyName("post_like") @set:PropertyName("post_like")
    var postLike: List<String>? = null,

    @get:PropertyName("start_time") @set:PropertyName("start_time")
    var start_time: Timestamp? = null,

    @get:PropertyName("user_config") @set:PropertyName("user_config")
    var userConfig: UserConfigEntity? = null,

    @get:PropertyName("clear_Achievements_List") @set:PropertyName("clear_Achievements_List")
    var clearAchievementsList: List<String>? = null,

    @get:PropertyName("post_my") @set:PropertyName("post_my")
    var postMy: List<String>? = null,

    @get:PropertyName("comment_my") @set:PropertyName("comment_my")
    var commentMy: List<String>? = null,

    )