package com.agvber.kekkek.core.domain.model

import java.time.LocalDateTime

data class QuitSmokingTime(
    val startDateTime: LocalDateTime,
    val stopDateTime: LocalDateTime?
)