package com.stopsmoke.kekkek.presentation.post.reply

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
import com.stopsmoke.kekkek.domain.model.emptyComment
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
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val replyRepository: ReplyRepository,
    userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    val user: StateFlow<User?> = userRepository.getUserData()
        .catch { it.printStackTrace() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _replyId = MutableStateFlow<ReplyIdItem>(ReplyIdItem.init())
    val replyId = _replyId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val comment: StateFlow<Comment> = replyId.flatMapLatest { replyIdItem ->
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
    val reply: Flow<PagingData<Reply>> = replyId.flatMapLatest { replyIdItem ->
        if (replyIdItem.postId.isEmpty() || replyIdItem.commentId.isEmpty()) {
            return@flatMapLatest emptyFlow()
        }
        replyRepository.getReply(commentId = replyIdItem.commentId, postId = replyIdItem.postId)
    }.map { pagingModel->
        pagingModel.insertSeparators { before: Reply?, after: Reply? ->
            if(before == null) return@insertSeparators emptyReply()
            return@insertSeparators null
        }
    }.catch {
        it.printStackTrace()
    }.cachedIn(viewModelScope)


    fun setReplyId(replyIdItem: ReplyIdItem){
        _replyId.value = replyIdItem
    }

    fun addReply(reply:String) = viewModelScope.launch{
        if(user.value is User.Registered) {
            val user = user.value as User.Registered
            replyRepository.addReply(
                reply = Reply(
                    id = "",
                    written =  Written(
                        uid = user.uid,
                        name = user.name ,
                        profileImage = user.profileImage,
                        ranking = user.ranking
                    ),
                    likeUser = emptyList(),
                    unlikeUser = emptyList(),
                    dateTime = DateTime(LocalDateTime.now(), LocalDateTime.now()),
                    text = reply,
                    commentParent = comment.value.parent,
                    replyParent = comment.value.id
                )
            )
        }
    }

    fun deleteReply(reply: Reply) = viewModelScope.launch {
        replyRepository.deleteReply(reply)
    }

    fun commentLikeClick(comment: Comment) = viewModelScope.launch{
        commentRepository.setCommentItem(comment)
    }

    fun updateReply(reply: Reply) = viewModelScope.launch{
        replyRepository.updateReply(reply)
    }
}

