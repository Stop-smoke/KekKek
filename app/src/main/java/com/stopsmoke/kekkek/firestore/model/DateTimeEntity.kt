package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class DateTimeEntity(
    @get:PropertyName("created") @set:PropertyName("created")
    var created: Timestamp? = null,
    @get:PropertyName("modified") @set:PropertyName("modified")
    var modified: Timestamp? = null
)