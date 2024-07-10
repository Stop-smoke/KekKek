package com.stopsmoke.kekkek.presentation.notification.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.model.Notification
import com.stopsmoke.kekkek.core.domain.model.NotificationCategory
import com.stopsmoke.kekkek.databinding.RecyclerviewNotificationItemBinding
import com.stopsmoke.kekkek.presentation.toResourceId

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