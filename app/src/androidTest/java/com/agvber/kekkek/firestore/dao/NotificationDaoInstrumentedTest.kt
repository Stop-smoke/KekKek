package com.agvber.kekkek.firestore.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.agvber.kekkek.core.firestore.dao.NotificationDao
import com.agvber.kekkek.core.firestore.data.NotificationDaoImpl
import com.agvber.kekkek.core.firestore.model.NotificationEntity
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class NotificationDaoInstrumentedTest {

    private lateinit var notificationDao: NotificationDao

    @Before
    fun init() {
        notificationDao = NotificationDaoImpl(Firebase.firestore)
    }

    @Test
    fun generateNotificationItems() = runTest {
        generateDummyNotifications(120).forEach {
            notificationDao.addNotificationItem(it)
        }
    }

    private fun generateRandomDateTime(): String {
        val now = LocalDateTime.now()
        val randomDays = Random.nextLong(0, 30)
        val randomHours = Random.nextLong(0, 24)
        val randomMinutes = Random.nextLong(0, 60)
        val randomSeconds = Random.nextLong(0, 60)
        val randomDateTime = now.minusDays(randomDays).minusHours(randomHours)
            .minusMinutes(randomMinutes).minusSeconds(randomSeconds)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return randomDateTime.format(formatter)
    }

    private fun generateDummyNotifications(count: Int): List<NotificationEntity> {
        val categories = listOf("information, community")
        return List(count) {
            NotificationEntity(
                id = null,
                uid = "default",
                title = listOf(
                    "회원님의 게시글에 새 댓글이 달렸습니다.",
                    "현재 회원님이 계시는 곳의 새로운 금연정보를 확인하세요!",
                    "회원님의 게시글에 새 댓글이 달렸습니다."
                ).let {
                    it[Random.nextInt(it.lastIndex)]
                },
                body = listOf(
                    "진짜 담배 어떻게 끊으셨는지 궁금 궁금 매우 궁금합니다",
                    "경기도 성남시 분당구",
                ).let {
                    it[Random.nextInt(it.lastIndex)]
                },
//                dateTime = generateRandomDateTime(),
                category = categories.random()
            )
        }
    }
}