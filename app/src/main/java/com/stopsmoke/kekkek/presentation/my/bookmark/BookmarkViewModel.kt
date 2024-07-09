package com.stopsmoke.kekkek.presentation.my.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.domain.repository.BookmarkRepository
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

    val post: Flow<PagingData<Post>> = bookmarkRepository.getBookmarkItems()
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

}