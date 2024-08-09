package com.stopsmoke.kekkek.presentation.reply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.common.exception.GuestModeException
import com.stopsmoke.kekkek.core.domain.model.Reply
import com.stopsmoke.kekkek.core.domain.model.emptyReply
import com.stopsmoke.kekkek.core.domain.repository.CommentRepository
import com.stopsmoke.kekkek.core.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.core.domain.usecase.AddReplyUseCase
import com.stopsmoke.kekkek.core.domain.usecase.GetUserDataUseCase
import com.stopsmoke.kekkek.presentation.model.UserUiState
import com.stopsmoke.kekkek.presentation.toggleElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val replyRepository: ReplyRepository,
    userRepository: UserRepository,
    private val commentRepository: CommentRepository,
    private val addReplyUseCase: AddReplyUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
) : ViewModel() {

    val user: StateFlow<UserUiState> = userRepository.getUserData()
        .asResult()
        .map {
            when (it) {
                is Result.Error -> {
                    if (it.exception is GuestModeException) {
                        return@map UserUiState.Guest
                    }
                    UserUiState.Error(it.exception)
                }

                is Result.Loading -> UserUiState.Loading
                is Result.Success -> UserUiState.Registered(it.data)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserUiState.Loading
        )

    private val _postId: MutableStateFlow<String> = MutableStateFlow("")
    val postId = _postId.asStateFlow()

    fun updatePostId(id: String) {
        viewModelScope.launch {
            _postId.emit(id)
        }
    }

    private val _commentId: MutableStateFlow<String> = MutableStateFlow("")
    val commentId = _commentId.asStateFlow()

    fun updateCommentId(id: String) {
        viewModelScope.launch {
            _commentId.emit(id)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment = postId.zip(commentId) { postId, commentId ->
        if (postId.isBlank() || commentId.isBlank()) {
            return@zip emptyList()
        }

        listOf(postId, commentId)
    }
        .flatMapLatest {
            val postId = it.getOrNull(0) ?: return@flatMapLatest emptyFlow()
            val commentId = it.getOrNull(1) ?: return@flatMapLatest emptyFlow()

            commentRepository.getComment(postId, commentId)
        }
        .asResult()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Result.Loading
        )

    private val replyDeleteItemsId = MutableStateFlow<Set<String>>(emptySet())
    private val reverseReplyLikeId = MutableStateFlow<Set<String>>(emptySet())

    @OptIn(ExperimentalCoroutinesApi::class)
    val reply = commentId.flatMapLatest {
        if (it.isBlank()) {
            return@flatMapLatest emptyFlow()
        }
        replyRepository.getReply(it)
    }
        .map { pagingModel ->
            pagingModel.insertSeparators { before: Reply?, after: Reply? ->
                if (before == null) return@insertSeparators emptyReply()
                return@insertSeparators null
            }
        }
        .cachedIn(viewModelScope)
        .combine(replyDeleteItemsId) { pagingData, id ->
            pagingData.map { reply ->
                if (id.contains(reply.id)) {
                    return@map ReplyUiState.ItemDeleted
                }
                ReplyUiState.ReplyType(reply)
            }
        }
        .combine(reverseReplyLikeId) { pagingData, id ->
            pagingData.map { reply ->
                if (reply is ReplyUiState.ReplyType && id.contains(reply.data.id)) {
                    val newReply = reply.data.copy(
                        isLiked = !reply.data.isLiked,
                        likeUser = reply.data.likeUser.toggleElement(getUserDataUseCase().first().uid)
                    )
                    return@map reply.copy(newReply)
                }
                reply
            }
        }
        .catch { it.printStackTrace() }

    private val _previewReply = MutableStateFlow<List<Reply>>(emptyList())
    val previewReply: StateFlow<List<Reply>> = _previewReply.asStateFlow()

    fun addReply(reply: String) = viewModelScope.launch {
        try {
            val replyId = addReplyUseCase(
                post = (comment.value as Result.Success).data.parent,
                commentId = (comment.value as Result.Success).data.id,
                text = reply
            )

            val newReply = replyRepository.getReply(postId.value, commentId.value, replyId)
            _previewReply.update {
                it.toMutableList().apply { add(newReply.first()) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteReply(reply: Reply) = viewModelScope.launch {
        try {
            val previewItem = previewReply.value.find { it.id == reply.id }

            if (previewItem == null) {
                replyDeleteItemsId.update { it.toggleElement(reply.id) }
            } else {
                _previewReply.update { it.toMutableList().apply { remove(reply) } }
            }

            replyRepository.deleteReply(postId.value, commentId.value, reply.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setCommentLike(like: Boolean) = viewModelScope.launch {
        try {
            if (like) {
                commentRepository.addCommentLike(postId.value, commentId.value)
                return@launch
            }
            commentRepository.removeCommentLike(postId.value, commentId.value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setReplyLike(replyId: String, like: Boolean) = viewModelScope.launch {
        try {
            val previewItem = previewReply.value.find { it.id == replyId }

            if (previewItem != null) {
                _previewReply.update {
                    it.toMutableList().apply {
                        val newItem = previewItem.copy(
                            isLiked = !previewItem.isLiked,
                            likeUser = previewItem.likeUser.toggleElement(getUserDataUseCase().first().uid)
                        )
                        set(indexOf(previewItem), newItem)
                    }
                }
            } else {
                reverseReplyLikeId.emit(reverseReplyLikeId.value.toggleElement(replyId))
            }

            if (like) {
                replyRepository.appendReplyLike(postId.value, commentId.value, replyId)
                return@launch
            }
            replyRepository.removeReplyLike(postId.value, commentId.value, replyId)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

