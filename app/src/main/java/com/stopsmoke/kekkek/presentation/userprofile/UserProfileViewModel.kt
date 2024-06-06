package com.stopsmoke.kekkek.presentation.userprofile

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
    postRepository: PostRepository,
    commentRepository: CommentRepository,
) : ViewModel() {

    private val uid = savedStateHandle.getStateFlow("uid", "")

    private val _errorHandler = MutableSharedFlow<Unit>()
    val errorHandler get() = _errorHandler.asSharedFlow()


    val user: Flow<User> = userRepository.getUserData()
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

    val posts: Flow<PagingData<Post>> = postRepository.getPost()
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
        .cachedIn(viewModelScope)

    val myCommentHistory: Flow<PagingData<Comment>> =
        commentRepository.getCommentItems(CommentFilter.Me)
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
            .cachedIn(viewModelScope)

}