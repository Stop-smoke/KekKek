package com.stopsmoke.kekkek.core.firestore.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class UserConfigEntity(
    @get:PropertyName("daily_cigarettes_smoked") @set:PropertyName("daily_cigarettes_smoked")
    var dailyCigarettesSmoked: Int? = null,

    @get:PropertyName("pack_cigarette_count") @set:PropertyName("pack_cigarette_count")
    var packCigaretteCount: Int? = null,

    @get:PropertyName("pack_price") @set:PropertyName("pack_price")
    var packPrice: Int? = null,

    @get:PropertyName("birthday") @set:PropertyName("birthday")
    var birthDate: Timestamp? = null,
)