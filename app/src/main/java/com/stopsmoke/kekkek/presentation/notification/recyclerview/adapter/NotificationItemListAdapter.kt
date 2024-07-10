package com.stopsmoke.kekkek.presentation.notification.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.stopsmoke.kekkek.core.domain.model.Notification
import com.stopsmoke.kekkek.databinding.RecyclerviewNotificationItemBinding
import com.stopsmoke.kekkek.presentation.notification.recyclerview.viewholder.NotificationViewHolder
import com.stopsmoke.kekkek.presentation.utils.diffutil.NotificationDiffUtil

class NotificationItemListAdapter :
    PagingDataAdapter<Notification, NotificationViewHolder>(NotificationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = RecyclerviewNotificationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}