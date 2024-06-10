package com.stopsmoke.kekkek.domain.model

import java.time.LocalDateTime

data class UserConfig(
    val dailyCigarettesSmoked: Int,
    val packCigaretteCount: Int,
    val packPrice: Int,
    val birthDate: LocalDateTime,
)
