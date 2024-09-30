package com.agvber.kekkek.presentation.my.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.agvber.kekkek.common.asResult
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.repository.PostRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MyPostViewModel @Inject constructor(
    private val postRepository: PostRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.getUserData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val post = user.flatMapLatest { user ->
        postRepository.getPost(uid = (user as? User.Registered)?.uid ?: "")
    }.cachedIn(viewModelScope)
        .asResult()


    private var currentUserJob: Job? = null
}