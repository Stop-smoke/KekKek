package com.stopsmoke.kekkek.core.domain.model

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val image: String,
    val category: DatabaseCategory,
    val maxProgress: Int,
    val requestCode: String
)