package com.agvber.kekkek.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class RankingEntity(
    @PropertyName("id")
    val id: String? = null,
    @PropertyName("uid")
    val uid: String? = null,
    @PropertyName("introduction")
    val introduction: String? = null,
    @get:PropertyName("date_time") @set:PropertyName("date_time")
    var dateTime: DateTimeEntity? = null
)
