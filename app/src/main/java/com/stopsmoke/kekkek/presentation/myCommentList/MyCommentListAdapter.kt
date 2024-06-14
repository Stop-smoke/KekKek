package com.stopsmoke.kekkek.presentation.myCommentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewMyCommentItemBinding
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentPostData
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener

class MyCommentListAdapter : PagingDataAdapter<Comment, MyCommentListAdapter.ViewHolder>(diffUtil) {

    private var callback: CommunityCallbackListener? = null

    fun registerCallbackListener(callback: CommunityCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }

    class ViewHolder(
        private val binding: RecyclerviewMyCommentItemBinding,
        private val callback: CommunityCallbackListener?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) = with(binding) {
            tvMyCommentContent.text = item.postData.postTitle
            tvMyCommentDatetime.text = item.dateTime.created.run {
                "$year-${"%02d".format(monthValue)}-${"%02d".format(dayOfMonth)}"
            }
            tvMyCommentState.text = getCommentStateString(item.postData)

            binding.root.setOnClickListener {
                callback?.navigateToPost(item.postData.postId)
            }
        }

        private fun getCommentStateString(item: CommentPostData): String =
            item.let { "${it.postType}에 등록한 ${it.postTitle} 게시글에 댓글을 남겼습니다." }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return ViewHolder(
            RecyclerviewMyCommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {

        val diffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(
                oldItem: Comment,
                newItem: Comment,
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Comment,
                newItem: Comment,
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}