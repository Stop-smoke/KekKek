package com.stopsmoke.kekkek.presentation.post.model

import com.stopsmoke.kekkek.domain.model.Comment

sealed interface PostViewCommentRecyclerViewUiState {

    data object Header : PostViewCommentRecyclerViewUiState

    data class CommentType(val item: Comment) : PostViewCommentRecyclerViewUiState

    data class ReplyType(val item: Comment) : PostViewCommentRecyclerViewUiState

}