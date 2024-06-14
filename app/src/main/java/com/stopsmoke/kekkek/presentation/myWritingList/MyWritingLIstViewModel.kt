package com.stopsmoke.kekkek.presentation.myWritingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MyWritingLIstViewModel @Inject constructor(
    private val postRepository: PostRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.getUserData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val post = user.flatMapLatest { user ->
        postRepository.getPost(uid = (user as User.Registered).uid)
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)
}