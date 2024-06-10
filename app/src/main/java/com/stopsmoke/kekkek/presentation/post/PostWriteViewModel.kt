package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.PostWrite
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostWriteViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    fun addPost(post: PostWrite) {
        viewModelScope.launch {
            postRepository.addPost(post)
        }
    }


}