package com.agvber.kekkek.presentation.post.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agvber.kekkek.databinding.ItemCommentBinding
import com.agvber.kekkek.databinding.RecyclerviewEmptyBinding
import com.agvber.kekkek.databinding.RecyclerviewPostViewReplyBinding
import com.agvber.kekkek.databinding.RecyclerviewPostviewContentBinding
import com.agvber.kekkek.presentation.post.detail.callback.PostCommentCallback
import com.agvber.kekkek.presentation.post.detail.model.PostContentItem
import com.agvber.kekkek.presentation.post.detail.model.PostDetailCommentRecyclerViewUiState
import com.agvber.kekkek.presentation.post.detail.viewholder.PostCommentViewHolder
import com.agvber.kekkek.presentation.post.detail.viewholder.PostContentViewHolder
import com.agvber.kekkek.presentation.post.detail.viewholder.RecyclerviewEmptyViewHolder
import com.agvber.kekkek.presentation.post.detail.viewholder.PostReplyViewHolder
import com.agvber.kekkek.presentation.utils.diffutil.PostDetailCommentRecyclerViewUiStateDiffUtil

private enum class PostDetailType {
    POST, COMMENT, REPLY, DELETED
}

class PostDetailAdapter :
    PagingDataAdapter<PostDetailCommentRecyclerViewUiState, RecyclerView.ViewHolder>(
        PostDetailCommentRecyclerViewUiStateDiffUtil()
    ) {

    private var callback: PostCommentCallback? = null

    fun registerCallback(postCommentCallback: PostCommentCallback) {
        callback = postCommentCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    private var headerItem = PostContentItem(null, null)

    fun updatePostHeader(postHeaderItem: PostContentItem) {
        headerItem = postHeaderItem
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            PostDetailType.POST.ordinal -> {
                val view = RecyclerviewPostviewContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PostContentViewHolder(view, callback)
            }

            PostDetailType.COMMENT.ordinal -> {
                val view = ItemCommentBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                PostCommentViewHolder(view, callback)
            }

            PostDetailType.REPLY.ordinal -> {
                val view = RecyclerviewPostViewReplyBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                PostReplyViewHolder(view, callback)
            }

            PostDetailType.DELETED.ordinal -> {
                val view = RecyclerviewEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RecyclerviewEmptyViewHolder(view)
            }

            else -> throw IllegalStateException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return

        when (holder) {
            is PostContentViewHolder -> {
                holder.bind(headerItem)
            }

            is PostCommentViewHolder -> {
                holder.bind((item as PostDetailCommentRecyclerViewUiState.CommentType).item)
            }

            is PostReplyViewHolder -> {
                val uiState = item as PostDetailCommentRecyclerViewUiState.ReplyType
                holder.bind(uiState.item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PostDetailCommentRecyclerViewUiState.Header -> PostDetailType.POST.ordinal
            is PostDetailCommentRecyclerViewUiState.CommentType -> PostDetailType.COMMENT.ordinal
            is PostDetailCommentRecyclerViewUiState.ReplyType -> PostDetailType.REPLY.ordinal
            else -> PostDetailType.DELETED.ordinal
        }
    }
}