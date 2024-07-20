package com.stopsmoke.kekkek.presentation.utils.diffutil

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.stopsmoke.kekkek.presentation.reply.ReplyUiState

class ReplyUiStateDiffUtil : DiffUtil.ItemCallback<ReplyUiState>() {
    override fun areItemsTheSame(oldItem: ReplyUiState, newItem: ReplyUiState): Boolean {
        return when(oldItem) {
            is ReplyUiState.ItemDeleted -> {
                when (newItem) {
                    is ReplyUiState.ItemDeleted -> true
                    is ReplyUiState.ReplyType -> false
                }
            }
            is ReplyUiState.ReplyType -> {
                when (newItem) {
                    is ReplyUiState.ItemDeleted -> false
                    is ReplyUiState.ReplyType -> oldItem.data.id == newItem.data.id
                }
            }
        }
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ReplyUiState, newItem: ReplyUiState): Boolean {
        return oldItem === newItem
    }

}