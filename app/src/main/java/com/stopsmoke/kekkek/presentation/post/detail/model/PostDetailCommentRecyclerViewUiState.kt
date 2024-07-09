package com.stopsmoke.kekkek.presentation.post.detail.model

import com.stopsmoke.kekkek.core.domain.model.Comment

sealed interface PostDetailCommentRecyclerViewUiState {

    data object Header : PostDetailCommentRecyclerViewUiState

    data class CommentType(val item: Comment) : PostDetailCommentRecyclerViewUiState

    data class ReplyType(val item: Comment) : PostDetailCommentRecyclerViewUiState

    data object Deleted : PostDetailCommentRecyclerViewUiState

}