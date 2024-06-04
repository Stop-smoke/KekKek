package com.stopsmoke.kekkek.firestore.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.CommentDaoImpl
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentDaoInstrumentedTest {

    private lateinit var commentDao: CommentDao

    @Before
    fun init() {
        commentDao = CommentDaoImpl(Firebase.firestore)
    }


    @Test
    fun addComment() = runTest {
        val comment = CommentEntity(
            id = null,
            title = "Title $1",
            text = "This is the text of comment number 12.",
            dateTime = "2024-05-30T10:00:00",
            like = (0..100).random(),
            isLike = listOf(true, false).random(),
            unlike = (0..10).random(),
            isUnlike = listOf(true, false).random()
        )
        commentDao.addComment(comment)
    }

    @Test
    fun updateOrInsert() = runTest {
        val comment = CommentEntity(
            id = "0N5mD4ZENS88GfmdFBRe",
            title = "Title $1122222222222222222222222",
            text = "This is the text of comment number 12.",
            dateTime = "2024-05-30T10:00:00",
            like = (0..100).random(),
            isLike = listOf(true, false).random(),
            unlike = (0..10).random(),
            isUnlike = listOf(true, false).random()
        )
        commentDao.updateOrInsertComment(comment)
    }

    @Test
    fun delete() = runTest {
        commentDao.deleteComment("0N5mD4ZENS88GfmdFBRe")
    }

}