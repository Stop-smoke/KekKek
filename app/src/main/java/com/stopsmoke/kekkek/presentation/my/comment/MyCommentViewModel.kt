package com.stopsmoke.kekkek.presentation.my.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.model.CommentFilter
import com.stopsmoke.kekkek.core.domain.repository.CommentRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MyCommentViewModel @Inject constructor(
    private val commentRepository: CommentRepository,
    userRepository: UserRepository
) : ViewModel() {


    val user = userRepository.getUserData()


    @OptIn(ExperimentalCoroutinesApi::class)
    val post = user.flatMapLatest {
        commentRepository.getCommentItems(CommentFilter.User(it.uid))
    }.cachedIn(viewModelScope)
        .asResult()
}