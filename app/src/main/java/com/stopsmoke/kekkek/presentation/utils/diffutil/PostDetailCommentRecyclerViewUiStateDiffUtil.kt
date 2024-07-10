package com.stopsmoke.kekkek.presentation.utils.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.presentation.post.detail.model.PostDetailCommentRecyclerViewUiState

class PostDetailCommentRecyclerViewUiStateDiffUtil : DiffUtil.ItemCallback<PostDetailCommentRecyclerViewUiState>() {
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

    override fun areContentsTheSame(
        oldItem: PostDetailCommentRecyclerViewUiState,
        newItem: PostDetailCommentRecyclerViewUiState,
    ): Boolean {
        return oldItem === newItem
    }
}