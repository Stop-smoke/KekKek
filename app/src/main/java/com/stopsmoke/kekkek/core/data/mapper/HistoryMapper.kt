package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.domain.model.History
import com.stopsmoke.kekkek.core.domain.model.HistoryTime
import com.stopsmoke.kekkek.core.firestore.model.HistoryEntity
import com.stopsmoke.kekkek.core.firestore.model.HistoryTimeEntity
import java.time.LocalDateTime

internal fun HistoryEntity.asExternalModel() = History(
    historyTimeList = historyTimeList?.map { it.asExternalModel() } ?: emptyList(),
    totalMinutesTime = totalMinutesTime ?: 0
)

internal fun HistoryTimeEntity.asExternalModel() = HistoryTime(
    quitSmokingStartDateTime = quitSmokingStartDateTime?.toLocalDateTime() ?: LocalDateTime.now(),
    quitSmokingStopDateTime = quitSmokingStopDateTime?.toLocalDateTime() ?: null
)


internal fun History.toEntity() = HistoryEntity(
    historyTimeList = historyTimeList.map { it.toEntity() },
    totalMinutesTime = totalMinutesTime
)

internal fun HistoryTime.toEntity(): HistoryTimeEntity =
    HistoryTimeEntity(
        quitSmokingStartDateTime = quitSmokingStartDateTime.toFirebaseTimestamp(),
        quitSmokingStopDateTime = quitSmokingStopDateTime?.toFirebaseTimestamp() ?: null
    )

fun emptyHistory() = History(
    historyTimeList = emptyList(),
    totalMinutesTime = 0
)
