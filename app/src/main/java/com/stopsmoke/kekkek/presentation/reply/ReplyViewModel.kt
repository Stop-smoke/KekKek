package com.stopsmoke.kekkek.presentation.reply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.domain.model.emptyReply
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val replyRepository: ReplyRepository,
    userRepository: UserRepository,
    private val commentRepository: CommentRepository,
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.getUserData()
        .catch { it.printStackTrace() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
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
    val comment: StateFlow<Comment?> = postId.zip(commentId) { postId, commentId ->
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
        .catch {
            it.printStackTrace()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val reply: Flow<PagingData<Reply>> = commentId.flatMapLatest {
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
        .catch {
            it.printStackTrace()
        }

    fun addReply(reply: String) = viewModelScope.launch {
        try {
            if (comment.value == null) {
                return@launch
            }

            val user = user.value as? User.Registered ?: return@launch
            replyRepository.addReply(
                reply = Reply(
                    id = "",
                    written = Written(
                        uid = user.uid,
                        name = user.name,
                        profileImage = user.profileImage,
                        ranking = user.ranking
                    ),
                    likeUser = emptyList(),
                    unlikeUser = emptyList(),
                    dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
                    text = reply,
                    commentParent = comment.value!!.parent,
                    replyParent = comment.value!!.id,
                    isLiked = false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteReply(reply: Reply) = viewModelScope.launch {
        try {
            replyRepository.deleteReply(reply)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun commentLikeClick(updateComment: Comment) = viewModelScope.launch {
        try {
            commentRepository.setCommentItem(updateComment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateReply(reply: Reply) = viewModelScope.launch {
        try {
            replyRepository.updateReply(reply)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

