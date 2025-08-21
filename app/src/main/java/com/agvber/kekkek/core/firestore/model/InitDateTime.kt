package com.agvber.kekkek.core.firestore.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.PropertyName

data class InitDateTime(
    @get:PropertyName("created") @set:PropertyName("created")
    var created: FieldValue = FieldValue.serverTimestamp(),
    @get:PropertyName("modified") @set:PropertyName("modified")
    var modified: FieldValue = FieldValue.serverTimestamp(),
)
