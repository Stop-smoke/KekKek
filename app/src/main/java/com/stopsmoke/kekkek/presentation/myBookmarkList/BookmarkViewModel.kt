package com.stopsmoke.kekkek.presentation.myBookmarkList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository
) : ViewModel() {
    val _userState: MutableStateFlow<User> = MutableStateFlow(User.Guest)
    val userState = _userState.asStateFlow()

    val myBookmarkPosts = userState.flatMapLatest { userData ->
        postRepository.getBookmark(if (userData is User.Registered) userData.postBookmark else emptyList())
            .let {
                when (it) {
                    is Result.Error -> {
                        it.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData ->
                        pagingData.map { post ->
                            post.toCommunityWritingListItem()
                        }
                    }
                }
            }.cachedIn(viewModelScope)
    }

    fun updateUserState() = viewModelScope.launch {

        val userData = userRepository.getUserData()
        userData.collect { user ->
            try {
                when (user) {
                    is User.Registered -> {
                        _userState.value = user
                    }

                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun updatdeTestUserState() = viewModelScope.launch {
        try {
            val userDataResultFlow = userRepository.getUserData("테스트_계정")
            when (userDataResultFlow) {
                is Result.Success -> {
                    userDataResultFlow.data.collect {
                        _userState.value = it
                    }
                }

                else -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}