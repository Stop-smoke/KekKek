package com.stopsmoke.kekkek.presentation.post

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stopsmoke.kekkek.databinding.ItemCommentBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewPostViewReplyBinding
import com.stopsmoke.kekkek.databinding.RecyclerviewPostviewContentBinding
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.presentation.post.callback.PostCommentCallback
import com.stopsmoke.kekkek.presentation.post.model.PostViewCommentRecyclerViewUiState
import com.stopsmoke.kekkek.presentation.post.viewholder.PostCommentViewHolder
import com.stopsmoke.kekkek.presentation.post.viewholder.PostContentViewHolder
import com.stopsmoke.kekkek.presentation.post.viewholder.PostReplyViewHolder

private enum class PostViewType {
    POST, COMMENT, REPLY
}

data class PostHeaderItem(
    val user: User?,
    val post: Post?,
    val commentNum: Long,
)

class PostViewAdapter : PagingDataAdapter<PostViewCommentRecyclerViewUiState, RecyclerView.ViewHolder>(diffUtil) {

    private var callback: PostCommentCallback? = null

    fun registerCallback(postCommentCallback: PostCommentCallback) {
        callback = postCommentCallback
    }

    fun unregisterCallback() {
        callback = null
    }

    private var headerItem = PostHeaderItem(null, null, 0)

    fun updatePostHeader(postHeaderItem: PostHeaderItem) {
        headerItem = postHeaderItem
        notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            PostViewType.POST.ordinal -> {
                val view = RecyclerviewPostviewContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PostContentViewHolder(view, callback)
            }

            PostViewType.COMMENT.ordinal -> {
                val view = ItemCommentBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                PostCommentViewHolder(view, callback)
            }

            PostViewType.REPLY.ordinal -> {
                val view = RecyclerviewPostViewReplyBinding.inflate(
                    /* inflater = */ LayoutInflater.from(parent.context),
                    /* parent = */ parent,
                    /* attachToParent = */ false
                )
                PostReplyViewHolder(view, callback)
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
                holder.bind((item as PostViewCommentRecyclerViewUiState.CommentType).item)
            }

            is PostReplyViewHolder -> {
                val uiState = item as PostViewCommentRecyclerViewUiState.ReplyType
                holder.bind(uiState.item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)!!) {
            is PostViewCommentRecyclerViewUiState.Header -> PostViewType.POST.ordinal
            is PostViewCommentRecyclerViewUiState.CommentType -> PostViewType.COMMENT.ordinal
            is PostViewCommentRecyclerViewUiState.ReplyType -> PostViewType.REPLY.ordinal
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<PostViewCommentRecyclerViewUiState>() {
            override fun areItemsTheSame(
                oldItem: PostViewCommentRecyclerViewUiState,
                newItem: PostViewCommentRecyclerViewUiState,
            ): Boolean {
                return when(oldItem) {
                    is PostViewCommentRecyclerViewUiState.CommentType -> {
                        when(newItem) {
                            is PostViewCommentRecyclerViewUiState.CommentType -> oldItem.item.id == newItem.item.id
                            is PostViewCommentRecyclerViewUiState.Header -> false
                            is PostViewCommentRecyclerViewUiState.ReplyType -> oldItem.item.id == newItem.item.id
                        }
                    }
                    is PostViewCommentRecyclerViewUiState.Header -> {
                        when(newItem) {
                            is PostViewCommentRecyclerViewUiState.CommentType -> "Header" == newItem.item.id
                            is PostViewCommentRecyclerViewUiState.Header -> true
                            is PostViewCommentRecyclerViewUiState.ReplyType -> "Header" == newItem.item.id
                        }
                    }
                    is PostViewCommentRecyclerViewUiState.ReplyType -> {
                        when(newItem) {
                            is PostViewCommentRecyclerViewUiState.CommentType -> oldItem.item.id == newItem.item.id
                            is PostViewCommentRecyclerViewUiState.Header -> false
                            is PostViewCommentRecyclerViewUiState.ReplyType -> oldItem.item.id == newItem.item.id
                        }
                    }
                }
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: PostViewCommentRecyclerViewUiState,
                newItem: PostViewCommentRecyclerViewUiState,
            ): Boolean {
                return oldItem === newItem
            }
        }
    }
}