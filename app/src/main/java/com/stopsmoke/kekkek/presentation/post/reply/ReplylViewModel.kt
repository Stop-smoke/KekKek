package com.stopsmoke.kekkek.presentation.post.reply

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.firestore.data.ReplyDaoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReplylViewModel @Inject constructor(
    private val replyDaoImpl: ReplyDaoImpl
) : ViewModel() {

}