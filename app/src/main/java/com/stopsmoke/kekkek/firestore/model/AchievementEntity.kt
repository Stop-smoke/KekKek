package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class AchievementEntity(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String? = null,

    @get:PropertyName("name") @set:PropertyName("name")
    var name: String? = null,

    @get:PropertyName("description") @set:PropertyName("description")
    var description: String? = null,

    @get:PropertyName("image") @set:PropertyName("image")
    var image: String? = null,

    @get:PropertyName("category") @set:PropertyName("category")
    var category: String? = null,

    @get:PropertyName("max_progress") @set:PropertyName("max_progress")
    var maxProgress: Int? = null,

    @get:PropertyName("request_code") @set:PropertyName("request_code")
    var requestCode: String? = null,
    )
