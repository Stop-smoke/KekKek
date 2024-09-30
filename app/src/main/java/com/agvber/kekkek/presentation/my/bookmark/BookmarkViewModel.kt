package com.agvber.kekkek.presentation.my.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.agvber.kekkek.common.asResult
import com.agvber.kekkek.core.domain.repository.BookmarkRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    userRepository: UserRepository,
    bookmarkRepository: BookmarkRepository,
) : ViewModel() {

    val user = userRepository.getUserData()

    val post = bookmarkRepository.getBookmarkItems()
        .cachedIn(viewModelScope)
        .asResult()

}