package com.stopsmoke.kekkek.algolia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WrittenEntity(
    @SerialName("uid")
    var uid: String? = null,

    @SerialName("name")
    var name: String? = null,

    @SerialName("profile_image")
    var profileImage: String? = null,

    @SerialName("smoking_start_date_time")
    var smokingStartDateTime: Long? = null,
)