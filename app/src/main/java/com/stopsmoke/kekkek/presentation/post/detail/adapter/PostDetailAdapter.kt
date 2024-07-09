package com.stopsmoke.kekkek.presentation.post.detail.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewEmptyBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewPostViewReplyBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewPostviewContentBinding
import com.stopsmoke.kekkek.presentation.post.detail.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.post.detail.model.PostContentItem
import com.stopsmoke.kekkek.presentation.post.detail.model.PostDetailCommentRecyclerViewUiState
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PostCommentViewHolder
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PostContentViewHolder
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PostDeletedItemAdapter
import com.stopsmoke.kekkek.presentation.post.detail.viewholder.PostReplyViewHolder

private enum class PostDetailType {
    POST, COMMENT, REPLY, DELETED
}

class PostDetailAdapter :
    PagingDataAdapter<PostDetailCommentRecyclerViewUiState, RecyclerView.ViewHolder>(diffUtil) {

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
                PostDeletedItemAdapter(view)
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

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<PostDetailCommentRecyclerViewUiState>() {
                override fun areItemsTheSame(
                    oldItem: PostDetailCommentRecyclerViewUiState,
                    newItem: PostDetailCommentRecyclerViewUiState,
                ): Boolean {
                    return when (oldItem) {
                        is PostDetailCommentRecyclerViewUiState.CommentType -> {
                            when (newItem) {
                                is PostDetailCommentRecyclerViewUiState.CommentType -> oldItem.item.id == newItem.item.id
                                is PostDetailCommentRecyclerViewUiState.Header -> false
                                is PostDetailCommentRecyclerViewUiState.ReplyType -> oldItem.item.id == newItem.item.id
                                PostDetailCommentRecyclerViewUiState.Deleted -> false
                            }
                        }

                        is PostDetailCommentRecyclerViewUiState.Header -> {
                            when (newItem) {
                                is PostDetailCommentRecyclerViewUiState.CommentType -> "Header" == newItem.item.id
                                is PostDetailCommentRecyclerViewUiState.Header -> true
                                is PostDetailCommentRecyclerViewUiState.ReplyType -> "Header" == newItem.item.id
                                PostDetailCommentRecyclerViewUiState.Deleted -> false
                            }
                        }

                        is PostDetailCommentRecyclerViewUiState.ReplyType -> {
                            when (newItem) {
                                is PostDetailCommentRecyclerViewUiState.CommentType -> oldItem.item.id == newItem.item.id
                                is PostDetailCommentRecyclerViewUiState.Header -> false
                                is PostDetailCommentRecyclerViewUiState.ReplyType -> oldItem.item.id == newItem.item.id
                                PostDetailCommentRecyclerViewUiState.Deleted -> false
                            }
                        }

                        PostDetailCommentRecyclerViewUiState.Deleted -> true
                    }
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: PostDetailCommentRecyclerViewUiState,
                    newItem: PostDetailCommentRecyclerViewUiState,
                ): Boolean {
                    return oldItem === newItem
                }
            }
    }
}