package com.stopsmoke.kekkek.presentation.post.reply

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.domain.model.Reply

class ReplyListAdapter
    : ListAdapter<Reply, ReplyListAdapter.ViewHolder>(diffCallback) {

    abstract class ViewHolder(
        root: View)
        : RecyclerView.ViewHolder(root) {
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Reply>() {
            override fun areItemsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Reply, newItem: Reply): Boolean {
                return oldItem.id == newItem.id

            }
        }
    }
}