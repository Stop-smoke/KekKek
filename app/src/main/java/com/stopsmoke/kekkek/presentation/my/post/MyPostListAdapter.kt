package com.stopsmoke.kekkek.presentation.my.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener
import com.stopsmoke.kekkek.presentation.utils.diffutil.PostDiffUtil

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