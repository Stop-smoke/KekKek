package com.agvber.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.agvber.kekkek.core.domain.model.Notification

class NotificationDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem === newItem
    }

}