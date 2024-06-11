package com.stopsmoke.kekkek.firestore.dao

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.PostDaoImpl
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import kotlin.random.Random

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
    }

//    @Test // 게시글 등록
//    fun addPost() = runTest {
//        val post = PostEntity(
//            id = "dummyId",
//            commentId = "dummyCommentId",
//            written = PostEntity.Written(
//                uid = "default",
//                name = "default",
//                profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
//                ranking = Random.nextLong(1, 500),
//            ),
//            title = "Dummy Title",
//            text = "Dummy Text",
//            dateTime = DateTimeEntity(
//                created = Timestamp.now(),
//                modified = Timestamp.now(),
//            ),
//            likeUser = listOf("likeUser1", "likeUser2"),
//            unlikeUser = listOf("unlikeUser1", "unlikeUser2"),
//            category = "dummyCategory",
//            views = Random.nextLong(1, 500),
//            commentUser = listOf("commentUser1", "commentUser2")
//        )
//        postDao.addPost(post)
//    }
//
//    @Test
//    fun updatePost() = runTest {
//        val post = PostEntity(
//            id = "dummyId",
//            commentId = "dummyCommentId",
//            written = PostEntity.Written(
//                uid = "default",
//                name = "default",
//                profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
//                ranking = Random.nextLong(1, 500),
//            ),
//            title = "Dummy Title",
//            text = "Dummy Text",
//            dateTime = DateTimeEntity(
//                created = Timestamp.now(),
//                modified = Timestamp.now(),
//            ),
//            likeUser = listOf("likeUser1", "likeUser2"),
//            unlikeUser = listOf("unlikeUser1", "unlikeUser2"),
//            category = "dummyCategory",
//            views = Random.nextLong(1, 500),
//            commentUser = listOf("commentUser1", "commentUser2")
//        )
//        postDao.updateOrInsertPost(post)
//    }

//    @Test
//    fun deletePost() = runTest {
//        val id = "lOMR60vdOoIx2gGSjZr2"
//        postDao.deletePost(id)
//    }
}