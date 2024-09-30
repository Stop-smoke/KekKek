package com.agvber.kekkek.presentation.my.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.agvber.kekkek.common.asResult
import com.agvber.kekkek.core.domain.model.CommentFilter
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.repository.CommentRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
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
        val user = it as User.Registered
        commentRepository.getCommentItems(CommentFilter.User(user.uid))
    }.cachedIn(viewModelScope)
        .asResult()
}