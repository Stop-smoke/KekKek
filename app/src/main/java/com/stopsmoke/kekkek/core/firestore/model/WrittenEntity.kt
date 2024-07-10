package com.stopsmoke.kekkek.core.firestore.model

import com.google.firebase.firestore.PropertyName

data class WrittenEntity(
    @get:PropertyName("uid") @set:PropertyName("uid")
    var uid: String? = null,

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String? = null,

    @get:PropertyName("profile_image") @set:PropertyName("profile_image")
    var profileImage: String? = null,

    @get:PropertyName("ranking") @set:PropertyName("ranking")
    var ranking: Long? = null,
)