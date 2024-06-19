package com.stopsmoke.kekkek.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import com.google.android.material.snackbar.Snackbar
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.PostCategory
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

internal fun<T> Flow<T>.collectLatestWithLifecycle(
    lifecycle: Lifecycle,
    action: suspend (value: T) -> Unit
): Job {
    return lifecycle.coroutineScope.launch {
        this@collectLatestWithLifecycle.flowWithLifecycle(lifecycle).collectLatest(action)
    }
}

internal fun<T> Bundle.getParcelableAndroidVersionSupport(key: String, clazz: Class<T>) =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        getParcelable(key)
    }

internal fun PostCategory.getResourceString(context: Context) = when(this) {
    PostCategory.NOTICE -> context.getString(R.string.community_category_notice)
    PostCategory.QUIT_SMOKING_SUPPORT -> context.getString(R.string.community_category_quit_smoking_support)
    PostCategory.POPULAR -> context.getString(R.string.community_category_popular)
    PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> context.getString(R.string.community_category_quit_smoking_aids_reviews)
    PostCategory.SUCCESS_STORIES -> context.getString(R.string.community_category_success_stories)
    PostCategory.GENERAL_DISCUSSION -> context.getString(R.string.community_category_general_discussion)
    PostCategory.FAILURE_STORIES -> context.getString(R.string.community_category_failure_stories)
    PostCategory.RESOLUTIONS -> context.getString(R.string.community_category_resolutions)
    PostCategory.UNKNOWN -> context.getString(R.string.community_category_unknown)
    PostCategory.ALL -> context.getString(R.string.community_category_all)
}

internal fun View.snackbarLongShow(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}