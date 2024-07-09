package com.stopsmoke.kekkek.core.domain.model

import com.google.firebase.Timestamp
import java.time.LocalDateTime

data class UserConfig(
    val dailyCigarettesSmoked: Int,
    val packCigaretteCount: Int,
    val packPrice: Int,
    val birthDate: LocalDateTime = LocalDateTime.now(),
)
