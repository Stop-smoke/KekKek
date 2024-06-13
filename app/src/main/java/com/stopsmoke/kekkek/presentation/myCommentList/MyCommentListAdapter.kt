package com.stopsmoke.kekkek.presentation.myCommentList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.RecyclerviewMyCommentItemBinding
import com.stopsmoke.kekkek.presentation.community.CommunityCallbackListener

class MyCommentListAdapter(
    private val viewModel: MyCommentViewModel
) : PagingDataAdapter<MyCommentItem, MyCommentListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MyCommentItem>() {
        override fun areItemsTheSame(
            oldItem: MyCommentItem,
            newItem: MyCommentItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MyCommentItem,
            newItem: MyCommentItem
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

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
        private val viewModel: MyCommentViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyCommentItem) = with(binding) {
            tvMyCommentContent.text = item.content
            tvMyCommentDatetime.text = ""
            tvMyCommentState.text = getCommentStateString(item)

            binding.root.setOnClickListener {
                viewModel.setCommunityWritingItem(item.postData.postId)
                viewModel.getCommunityWritingItem()?.let {
                    callback?.navigateToPost(it)
                }
            }
        }

        private fun getCommentStateString(item: MyCommentItem): String =
            item.postData.let { "${it.postType}에 등록한 ${it.postTitle} 게시글에 댓글을 남겼습니다." }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            RecyclerviewMyCommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), callback, viewModel
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}