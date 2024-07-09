package com.stopsmoke.kekkek.presentation.my.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem

class BookmarkListAdapter : PagingDataAdapter<CommunityWritingItem, BookmarkListViewHolder>(diffUtil) {

    private var callback: CommunityCallbackListener? = null

    fun registerCallbackListener(callback: CommunityCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BookmarkListViewHolder {
        return BookmarkListViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback
        )
    }

    override fun onBindViewHolder(holder: BookmarkListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<CommunityWritingItem>() {
            override fun areItemsTheSame(
                oldItem: CommunityWritingItem,
                newItem: CommunityWritingItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CommunityWritingItem,
                newItem: CommunityWritingItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}