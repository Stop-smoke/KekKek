package com.stopsmoke.kekkek.presentation.post.reply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.emptyComment
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val replyRepository: ReplyRepository,
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.getUserData()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _commentId = MutableStateFlow<ReplyIdItem>(ReplyIdItem.init())
    val commentId = _commentId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment: Flow<Comment> = commentId.flatMapLatest { replyIdItem ->
        if (replyIdItem.postId.isEmpty() || replyIdItem.commentId.isEmpty()) {
            return@flatMapLatest emptyFlow()
        }
        commentRepository.getComment(commentId = replyIdItem.commentId, postId = replyIdItem.postId)
    }.catch {
        it.printStackTrace()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyComment()
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    val reply: Flow<PagingData<Reply>> = commentId.flatMapLatest { replyIdItem ->
        if (replyIdItem.postId.isEmpty() || replyIdItem.commentId.isEmpty()) {
            return@flatMapLatest emptyFlow()
        }
        replyRepository.getReply(commentId = replyIdItem.commentId, postId = replyIdItem.postId)
    }.catch {
        it.printStackTrace()
    }.cachedIn(viewModelScope)

}

