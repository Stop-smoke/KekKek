package com.stopsmoke.kekkek.presentation

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal fun ElapsedDateTime.toResourceId(context: Context): String =
    when (elapsedDateTime) {
        DateTimeUnit.YEAR -> "${number}${context.getString(R.string.notification_elapsed_year)}"
        DateTimeUnit.MONTH -> "${number}${context.getString(R.string.notification_elapsed_month)}"
        DateTimeUnit.WEEK -> "${number}${context.getString(R.string.notification_elapsed_week)}"
        DateTimeUnit.DAY -> "${number}${context.getString(R.string.notification_elapsed_day)}"
        DateTimeUnit.HOUR -> "${number}${context.getString(R.string.notification_elapsed_hour)}"
        DateTimeUnit.MINUTE -> "${number}${context.getString(R.string.notification_elapsed_minute)}"
        DateTimeUnit.SECOND -> "${number}${context.getString(R.string.notification_elapsed_second)}"
    }


internal fun<T> Flow<T>.collectLatest(
    lifecycleScope: LifecycleCoroutineScope,
    action: (value: T) -> Unit
): Job {
    return lifecycleScope.launch {
        this@collectLatest.collectLatest(action)
    }
}