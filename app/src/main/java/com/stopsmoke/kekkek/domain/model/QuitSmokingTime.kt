package com.stopsmoke.kekkek.domain.model

import java.time.LocalDateTime

data class QuitSmokingTime(
    val startDateTime: LocalDateTime,
    val stopDateTime: LocalDateTime?
)