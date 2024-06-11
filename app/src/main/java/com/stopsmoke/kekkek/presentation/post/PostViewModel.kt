package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel : ViewModel() {

    private val _newPost: MutableLiveData<PostWriteItem> = MutableLiveData()
    val newPost: LiveData<PostWriteItem> get() = _newPost

    fun setupLiveData(newPost: PostWriteItem) {
        _newPost.postValue(newPost)
    }

}