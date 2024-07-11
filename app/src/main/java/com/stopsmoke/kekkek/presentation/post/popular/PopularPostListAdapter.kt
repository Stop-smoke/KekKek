package com.stopsmoke.kekkek.presentation.post.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.utils.diffutil.CommunityWritingItemDiffUtil

class PopularPostListAdapter
    : ListAdapter<CommunityWritingItem, PopularPostListViewHolder>(CommunityWritingItemDiffUtil()) {

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
    ): PopularPostListViewHolder {
        return PopularPostListViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback
        )
    }

    override fun onBindViewHolder(holder: PopularPostListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}