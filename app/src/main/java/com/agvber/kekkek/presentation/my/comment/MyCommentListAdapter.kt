package com.agvber.kekkek.presentation.my.comment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.core.domain.model.Comment
import com.agvber.kekkek.core.domain.model.CommentParent
import com.agvber.kekkek.databinding.RecyclerviewMyCommentItemBinding
import com.agvber.kekkek.presentation.community.CommunityCallbackListener
import com.agvber.kekkek.presentation.mapper.getResourceString
import com.agvber.kekkek.presentation.utils.diffutil.CommentDiffUtil

class MyCommentListAdapter
    : PagingDataAdapter<Comment, MyCommentListAdapter.ViewHolder>(CommentDiffUtil()) {

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
            tvMyCommentContent.text = item.text
            tvMyCommentDatetime.text = item.dateTime.created.run {
                "$year-${"%02d".format(monthValue)}-${"%02d".format(dayOfMonth)}"
            }
            tvMyCommentState.text = getCommentStateString(item.parent)

            binding.root.setOnClickListener {
                callback?.navigateToPost(item.parent.postId)
            }
        }

        private fun getCommentStateString(item: CommentParent): String =
            "${item.postType.getResourceString(itemView.context)}에 등록한 ${item.postTitle} 게시글에 댓글을 남겼습니다."
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
}