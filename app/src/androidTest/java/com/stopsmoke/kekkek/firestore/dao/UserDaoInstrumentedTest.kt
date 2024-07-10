package com.stopsmoke.kekkek.firestore.dao

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.core.firestore.dao.UserDao
import com.stopsmoke.kekkek.core.firestore.data.UserDaoImpl
import com.stopsmoke.kekkek.core.firestore.model.HistoryEntity
import com.stopsmoke.kekkek.core.firestore.model.HistoryTimeEntity
import com.stopsmoke.kekkek.core.firestore.model.LocationEntity
import com.stopsmoke.kekkek.core.firestore.model.UserConfigEntity
import com.stopsmoke.kekkek.core.firestore.model.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class UserDaoInstrumentedTest {

    private lateinit var userDao: UserDao

    @Before
    fun init() {
        userDao = UserDaoImpl(Firebase.firestore)
    }

    @Test
    fun getUsersData() = runTest {
        val result = userDao.getUser("테스트_계정").first()
        Log.d("result", result.toString())
        Assert.assertEquals(result.uid, "테스트_계정")
    }

    @Test
    fun setUsersData() = runTest {
        val dateFormat = SimpleDateFormat("yyyy.MM.dd")

        val startDates = listOf(
            "2022.04.01",
            "2022.05.01",
            "2022.06.01",
            "2022.07.01",
            "2022.08.01"
        )

        val stopDates = listOf(
            "2022.04.05",
            "2022.05.05",
            "2022.06.05",
            "2022.07.05",
            null
        )

        val historyEntities = startDates.zip(stopDates).map { (start, stop) ->
            val startDate = dateFormat.parse(start)
            val stopDate = if(stop == null) null else dateFormat.parse(stop)
            HistoryTimeEntity(
                quitSmokingStartDateTime = Timestamp(startDate),
                quitSmokingStopDateTime = if(stopDate == null) null else Timestamp(stopDate)
            )
        }

        val user = UserEntity(
            uid = "테스트_계정",
            name = "김민준",
            nickname = "히히",
            gender = "male",
            age = 24,
            start_time = getRandomTimestamp(),
            location = LocationEntity(0, 0, "강북구"),
            phoneNumber = "01013456987",
            userConfig = UserConfigEntity(
                dailyCigarettesSmoked = 5,
                packCigaretteCount = 20,
                packPrice = 5500
            ),
            postMy = listOf("haha", "dummyPostId"),
            postBookmark = listOf("FIWO0bsaJxLz3MpFtCha", "MWdTXOEJ7RReWZuUarac"),
            commentMy = listOf("0SaeAiRM6MUnyMbUXcib"),
            history = HistoryEntity(
                historyTimeList = historyEntities,
                totalMinutesTime = 1000
            )
        )
        userDao.setUser(user)
        Assert.assertEquals(user, userDao.getUser("테스트_계정").first())
    }

    private fun getRandomTimestamp(): Timestamp {
        // 현재 시간을 가져오기 위해 Calendar 인스턴스 생성
        val calendar = Calendar.getInstance()

        // 7일 전으로 시간 설정
        calendar.add(Calendar.DAY_OF_YEAR, -7)

        // Calendar 객체에서 Date 객체로 변환
        val date = calendar.time

        // Date 객체를 사용하여 Timestamp 생성
        return Timestamp(date)
    }
}