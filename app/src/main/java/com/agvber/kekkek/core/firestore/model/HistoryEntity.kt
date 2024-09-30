package com.agvber.kekkek.core.firestore.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class HistoryEntity(
    @get:PropertyName("history_time_list") @set:PropertyName("history_time_list")
    var historyTimeList: List<HistoryTimeEntity>? = null,

    @get:PropertyName("total_minutes_time") @set:PropertyName("total_minutes_time")
    var totalMinutesTime: Long? = null,
)

data class HistoryTimeEntity(
    @get:PropertyName("quit_smoking_start_date_time") @set:PropertyName("quit_smoking_start_date_time")
    var quitSmokingStartDateTime: Timestamp? = null,
    @get:PropertyName("quit_smoking_stop_date_time") @set:PropertyName("quit_smoking_stop_date_time")
    var quitSmokingStopDateTime: Timestamp? = null
)