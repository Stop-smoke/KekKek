package com.stopsmoke.kekkek.presentation.my.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.utils.diffutil.CommunityWritingItemDiffUtil

class BookmarkListAdapter : PagingDataAdapter<CommunityWritingItem, BookmarkListViewHolder>(
    CommunityWritingItemDiffUtil()
) {

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
}