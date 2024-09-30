package com.agvber.kekkek.presentation.notification.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.model.Notification
import com.agvber.kekkek.core.domain.model.NotificationCategory
import com.agvber.kekkek.databinding.RecyclerviewNotificationItemBinding
import com.agvber.kekkek.presentation.toResourceId

class NotificationViewHolder(
    val binding: RecyclerviewNotificationItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(notification: Notification) = with(binding) {
        tvNotificationTitle.text = notification.title
        tvNotificationBody.text = notification.body
        tvNotificationDateTime.text = notification.modifiedElapsedDateTime.toResourceId(itemView.context)
        ivNotificationIcon.setImageResource(notification.category.toResourceId())
    }
}

private fun NotificationCategory.toResourceId(): Int = when (this) {
    NotificationCategory.COMMUNITY -> R.drawable.ic_notification_chat
    NotificationCategory.INFORMATION -> R.drawable.ic_notification_bell
    NotificationCategory.UNRECOGNIZABLE -> R.drawable.ic_notification_bell
}