package com.stopsmoke.kekkek.example

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.firestore.dao.CommentDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamplePagingViewModel @Inject constructor(
    commentDao: CommentDao, // repository 패턴 이라서 그대로 따라 하시면 안돼요!
) : ViewModel() {
//    val data = commentDao.getComment()
}