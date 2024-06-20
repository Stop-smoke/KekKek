package com.stopsmoke.kekkek.presentation.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Comment
import com.stopsmoke.kekkek.domain.model.CommentFilter
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    userRepository: UserRepository,
    postRepository: PostRepository,
    commentRepository: CommentRepository,
) : ViewModel() {

    private val _uid = MutableStateFlow<String?>(null)
    private val uid get() = _uid

    fun updateUid(uid: String) {
        viewModelScope.launch {
            _uid.emit(uid)
        }
    }

    private val _errorHandler = MutableSharedFlow<Unit>()
    val errorHandler get() = _errorHandler.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: Flow<User.Registered> = uid.flatMapLatest { userId ->
        if (userId == null) {
            return@flatMapLatest emptyFlow()
        }

        userRepository.getUserData(uid = userId)
            .let {
                when (it) {
                    is Result.Error -> {
                        viewModelScope.launch {
                            _errorHandler.emit(Unit)
                        }
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<Post>> = uid.flatMapLatest { uid ->
        if (uid == null) {
            return@flatMapLatest emptyFlow()
        }

        postRepository.getPost(uid)
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val myCommentHistory: Flow<PagingData<Comment>> = uid.flatMapLatest { uid ->
        if (uid == null) {
            return@flatMapLatest emptyFlow()
        }
        commentRepository.getCommentItems(CommentFilter.User(uid))
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

    private val _postItemClick = MutableSharedFlow<String>()
    val postItemClick = _postItemClick.asSharedFlow()

    fun clickPostItem(postId: String) {
        viewModelScope.launch {
            _postItemClick.emit(postId)
        }
    }
}