package com.stopsmoke.kekkek.presentation.my.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
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