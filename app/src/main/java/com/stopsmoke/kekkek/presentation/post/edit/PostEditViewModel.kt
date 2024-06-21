package com.stopsmoke.kekkek.presentation.post.edit

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {



}