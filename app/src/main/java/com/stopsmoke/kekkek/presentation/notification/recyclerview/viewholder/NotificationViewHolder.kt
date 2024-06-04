package com.stopsmoke.kekkek.presentation.notification.recyclerview.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.RecyclerviewNotificationItemBinding
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.domain.model.Notification
import com.stopsmoke.kekkek.domain.model.NotificationCategory

class NotificationViewHolder(
    val binding: RecyclerviewNotificationItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(notification: Notification) = with(binding) {
        tvNotificationTitle.text = notification.title
        tvNotificationBody.text = notification.body
        tvNotificationDateTime.text = notification.elapsedDateTime.toResourceId(itemView.context)
        ivNotificationIcon.setImageResource(notification.category.toResourceId())
    }
}

private fun NotificationCategory.toResourceId(): Int = when (this) {
    NotificationCategory.COMMUNITY -> R.drawable.ic_notification_chat
    NotificationCategory.INFORMATION -> R.drawable.ic_notification_bell
    NotificationCategory.UNRECOGNIZABLE -> R.drawable.ic_notification_bell
}

private fun ElapsedDateTime.toResourceId(context: Context): String =
    when (elapsedDateTime) {
        DateTimeUnit.YEAR -> "${number}${context.getString(R.string.notification_elapsed_year)}"
        DateTimeUnit.MONTH -> "${number}${context.getString(R.string.notification_elapsed_month)}"
        DateTimeUnit.WEEK -> "${number}${context.getString(R.string.notification_elapsed_week)}"
        DateTimeUnit.DAY -> "${number}${context.getString(R.string.notification_elapsed_day)}"
        DateTimeUnit.HOUR -> "${number}${context.getString(R.string.notification_elapsed_hour)}"
        DateTimeUnit.MINUTE -> "${number}${context.getString(R.string.notification_elapsed_minute)}"
        DateTimeUnit.SECOND -> "${number}${context.getString(R.string.notification_elapsed_second)}"
    }
