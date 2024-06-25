package com.stopsmoke.kekkek.algolia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateTimeEntity(
    @SerialName("created")
    val created: Long,
    @SerialName("modified")
    val modified: Long
)