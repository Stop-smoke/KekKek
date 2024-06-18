package com.stopsmoke.kekkek.firestore.dao

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.CommentDaoImpl
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import com.stopsmoke.kekkek.firestore.model.WrittenEntity
import kotlinx.coroutines.flow.first
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
            id = "12345",
            text = "This is a sample comment.",
            dateTime = DateTimeEntity(
                Timestamp.now(),
                Timestamp.now()
            ),
            likeUser = listOf("user1", "user2"),
            unlikeUser = listOf("user3"),
            reply = listOf(
                ReplyEntity(
                    written = WrittenEntity(
                        uid = "default",
                        name = "김민준",
                        profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
                        ranking = Long.MAX_VALUE
                    )
                ),
            ),
            written = WrittenEntity(
                uid = "default",
                name = "김민준",
                profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
                ranking = Long.MAX_VALUE
            )
        )
        commentDao.addComment(comment)
    }

    @Test
    fun updateOrInsert() = runTest {
        val comment = CommentEntity(
            id = "12345",
            text = "This is a sample comment.",
            dateTime = DateTimeEntity(
                Timestamp.now(),
                Timestamp.now()
            ),
            likeUser = listOf("user1", "user2"),
            unlikeUser = listOf("user3"),
            reply = listOf(
                ReplyEntity(
                    written = WrittenEntity(
                        uid = "default",
                        name = "김민준",
                        profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
                        ranking = Long.MAX_VALUE
                    )
                ),
            ),
            written = WrittenEntity(
                uid = "default",
                name = "김민준",
                profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
                ranking = Long.MAX_VALUE
            )
        )
        commentDao.updateOrInsertComment(comment)
    }

    @Test
    fun getCount() = runTest {

        commentDao.getCommentCount("zyr92XFIJxHRG72h2vSd")
            .first()
            .let {
                Log.d("TEST", it.toString())
            }

    }

    @Test
    fun delete() = runTest {
        commentDao.deleteComment("postId", "0N5mD4ZENS88GfmdFBRe")
    }
}