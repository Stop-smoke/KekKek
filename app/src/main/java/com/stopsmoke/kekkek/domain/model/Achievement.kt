package com.stopsmoke.kekkek.domain.model

import kotlin.random.Random

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val maxProgress: Int,
) {

    val currentProgress = Random.nextInt(0, maxProgress)

}