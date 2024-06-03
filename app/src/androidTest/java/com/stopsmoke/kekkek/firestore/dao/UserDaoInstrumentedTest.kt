package com.stopsmoke.kekkek.firestore.dao

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.UserDaoImpl
import com.stopsmoke.kekkek.firestore.model.LocationEntity
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

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
        val user = UserEntity(
            uid = "테스트_계정",
            name = "김민준",
            nickname = "히히",
            gender = "male",
            age = 24,
            location = LocationEntity(0, 0, "강북구"),
            phoneNumber = "01013456987"
        )
        userDao.setUser(user)
        Assert.assertEquals(user, userDao.getUser("테스트_계정").first())
    }
}