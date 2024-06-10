package com.stopsmoke.kekkek.domain.model

import kotlin.random.Random

data class Achievement(
    var id: String,
    var name: String,
    var content: String,
    var image: String,
    var category: DatabaseCategory,
    var maxProgress: Int,
)