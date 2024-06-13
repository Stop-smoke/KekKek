package com.stopsmoke.kekkek.domain.model

import com.google.firebase.Timestamp
import com.stopsmoke.kekkek.data.mapper.toLocalDateTime
import java.time.Duration
import java.time.LocalDateTime

data class History(
    var historyTimeList: List<HistoryTime>,
    var totalMinutesTime: Long,
)

data class HistoryTime(
    var quitSmokingStartDateTime: LocalDateTime,
    var quitSmokingStopDateTime: LocalDateTime?
)

fun History.getTotalMinutesTime(): Long {
    var totalMinutesTime = this.totalMinutesTime

    if (historyTimeList.isNotEmpty()) {
        val lastHistoryTime = historyTimeList.last()
        val lastQuitSmokingStopDateTime = lastHistoryTime.quitSmokingStopDateTime

        if (lastQuitSmokingStopDateTime == null) {
            totalMinutesTime += Duration.between(
                lastHistoryTime.quitSmokingStartDateTime,
                LocalDateTime.now()
            ).toMinutes()
        }
    }

    return totalMinutesTime
}

fun History.getTotalSecondsTime(): Long {
    var totalSecondsTime = this.totalMinutesTime * 60 // 분을 초로 변환

    if (historyTimeList.isNotEmpty()) {
        val lastHistoryTime = historyTimeList.last()
        val lastQuitSmokingStopDateTime = lastHistoryTime.quitSmokingStopDateTime

        if (lastQuitSmokingStopDateTime == null) {
            totalSecondsTime += Duration.between(
                lastHistoryTime.quitSmokingStartDateTime,
                LocalDateTime.now()
            ).seconds
        }
    }

    return totalSecondsTime
}



fun History.getStartTimerState(): Boolean {
    if (historyTimeList.isEmpty()) return false
    return historyTimeList.last().quitSmokingStopDateTime == null
}