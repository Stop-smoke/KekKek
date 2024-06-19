package com.stopsmoke.kekkek.presentation.myWritingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWritingLIstViewModel @Inject constructor(
    private val postRepository: PostRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.getUserData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val post = user.flatMapLatest { user ->
        postRepository.getPost(uid = (user as? User.Registered)?.uid ?: "")
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

    private var currentUserJob: Job? = null
}