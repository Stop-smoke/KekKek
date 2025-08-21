package com.agvber.kekkek.presentation.my.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.agvber.kekkek.core.domain.model.Post
import com.agvber.kekkek.databinding.ItemPostBinding
import com.agvber.kekkek.presentation.community.CommunityCallbackListener
import com.agvber.kekkek.presentation.utils.diffutil.PostDiffUtil

class MyPostListAdapter : PagingDataAdapter<Post, MyPostViewHolder>(PostDiffUtil()) {

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
    ): MyPostViewHolder {
        return MyPostViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback
        )
    }

    override fun onBindViewHolder(holder: MyPostViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}