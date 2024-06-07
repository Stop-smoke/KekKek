package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import java.time.LocalDateTime

internal fun DateTimeEntity?.asExternalModel(): DateTime =
    DateTime(
        created = this?.created?.toLocalDateTime() ?: LocalDateTime.MIN,
        modified = this?.modified?.toLocalDateTime() ?: LocalDateTime.MIN
    )