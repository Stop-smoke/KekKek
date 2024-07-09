package com.stopsmoke.kekkek.data.mapper

import android.os.Build
import com.google.firebase.Timestamp
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date

internal fun DateTimeEntity?.asExternalModel(): DateTime =
    DateTime(
        created = this?.created?.toLocalDateTime() ?: LocalDateTime.MIN,
        modified = this?.modified?.toLocalDateTime() ?: LocalDateTime.MIN
    )

internal fun DateTime.toEntity(): DateTimeEntity =
    DateTimeEntity(
        created = created.toFirebaseTimestamp(),
        modified = modified.toFirebaseTimestamp(),
    )

internal fun Timestamp.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(seconds, nanoseconds , ZonedDateTime.now().offset)

fun LocalDateTime.toFirebaseTimestamp(): Timestamp {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Timestamp(toInstant(ZoneOffset.UTC))
    } else {
        Timestamp(Date.from(toInstant(ZoneOffset.UTC)))
    }
}