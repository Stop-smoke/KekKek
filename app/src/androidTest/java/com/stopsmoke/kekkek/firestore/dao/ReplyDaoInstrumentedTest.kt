package com.stopsmoke.kekkek.firestore.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.ReplyDaoImpl
import com.stopsmoke.kekkek.firestore.model.CommentParentEntity
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import com.stopsmoke.kekkek.firestore.model.ReplyEntity
import com.stopsmoke.kekkek.firestore.model.WrittenEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReplyDaoInstrumentedTest {

    private lateinit var replyDao: ReplyDao

    @Before
    fun init() {
        replyDao = ReplyDaoImpl(Firebase.firestore)
    }

    @Test // 게시글 등록
    fun addPost() = runTest {
        val reply = ReplyEntity(
            id = "12345",
            text = "This is a sample comment.",
            dateTime = DateTimeEntity(
                Timestamp.now(),
                Timestamp.now()
            ),
            likeUser = listOf("user1", "user2"),
            unlikeUser = listOf("user3"),
            written = WrittenEntity(
                uid = "default",
                name = "김민준",
                profileImage = "https://file.notion.so/f/f/d2c6a42f-73c9-4a08-a064-629644e1df36/5dd968b8-2ed6-4a11-835d-a3bd71618151/Untitled.png?id=afb9acf4-f456-4b1b-8ab4-8e0bcd14002b&table=block&spaceId=d2c6a42f-73c9-4a08-a064-629644e1df36&expirationTimestamp=1717675200000&signature=WV4mFdY8JbZ45ZOBtMFcR5pMD_VrxqbFY7GlDCL0SU0&downloadName=Untitled.png",
                ranking = Long.MAX_VALUE
            ),
            replyParent = "DtDbSAuCmSlmTMsXX5lI",
            commentParent = CommentParentEntity(
                postId = "056DODKJ7FYMCaWxwnAr",
                postTitle = null,
                postType = null
            )
        )
        replyDao.addReply(reply)
    }
}