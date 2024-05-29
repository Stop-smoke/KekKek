package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class LocationEntity(
    @PropertyName("latitude") val latitude: Int? = null,
    @PropertyName("longitude") val longitude: Int? = null,
    @PropertyName("region") val region: String? = null,
)
