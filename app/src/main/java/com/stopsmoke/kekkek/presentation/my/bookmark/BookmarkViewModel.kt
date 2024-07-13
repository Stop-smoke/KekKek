package com.stopsmoke.kekkek.presentation.my.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.asResult
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.repository.BookmarkRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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