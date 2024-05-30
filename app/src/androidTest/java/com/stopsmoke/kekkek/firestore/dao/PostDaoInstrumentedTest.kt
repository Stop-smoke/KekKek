package com.stopsmoke.kekkek.firestore.dao

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.PostDaoImpl
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class PostDaoInstrumentedTest {

    private lateinit var postDao: PostDao

    @Before
    fun init() {
        postDao = PostDaoImpl(Firebase.firestore)
    }

    @Test // 게시글 가져오기
    fun getPost() = runTest {
        val post = postDao.getPost().first()
        Log.d("result", post.toString())
        Assert.assertTrue(post.isSuccess)
    }

    @Test // 게시글 등록
    fun addPost() = runTest {
        val post = PostEntity(
            id = "12345",
            title = "Sample Post",
            text = "This is a sample post for testing purposes.",
            dateTime = LocalDateTime.now().toString(),
            bookmark = 10,
            isBookmark = true,
            like = 50,
            isLike = true,
            unlike = 2,
            isUnlike = false,
            commentId = "cmt12345",
            writtenUid = "user123"
        )
        postDao.addPost(post)
    }

    @Test
    fun updatePost() = runTest {
        val post = PostEntity(
            id = "lOMR60vdOoIx2gGSjZr2",
            title = "Sample Post 1",
            text = "This is a sample post for testing purposes.",
            dateTime = LocalDateTime.now().toString(),
            bookmark = 10,
            isBookmark = true,
            like = 50,
            isLike = true,
            unlike = 2,
            isUnlike = false,
            commentId = "cmt12345",
            writtenUid = "user123"
        )
        postDao.updateOrInsertPost(post)
    }

    @Test
    fun deletePost() = runTest {
        val id = "lOMR60vdOoIx2gGSjZr2"
        postDao.deletePost(id)
    }
}