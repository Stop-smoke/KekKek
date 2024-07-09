package com.stopsmoke.kekkek.presentation.notification.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.databinding.RecyclerviewNotificationItemBinding
import com.stopsmoke.kekkek.core.domain.model.Notification
import com.stopsmoke.kekkek.presentation.notification.recyclerview.viewholder.NotificationViewHolder

class NotificationItemListAdapter :
    PagingDataAdapter<Notification, NotificationViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = RecyclerviewNotificationItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }

        }
    }
}