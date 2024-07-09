package com.stopsmoke.kekkek.presentation.my.post

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.databinding.ItemPostBinding
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener

class MyPostListAdapter : PagingDataAdapter<Post, MyPostViewHolder>(diffUtil) {

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

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(
                oldItem: Post,
                newItem: Post,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: Post,
                newItem: Post,
            ): Boolean {
                return oldItem === newItem
            }
        }
    }
}