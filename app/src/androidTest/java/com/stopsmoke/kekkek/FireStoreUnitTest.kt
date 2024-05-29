package com.stopsmoke.kekkek

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.FireStoreService
import com.stopsmoke.kekkek.firestore.model.LocationEntity
import com.stopsmoke.kekkek.firestore.model.UserEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FireStoreUnitTest {

    private lateinit var fireStoreService: FireStoreService

    @Before
    fun init() {
        val firestore = Firebase.firestore
        fireStoreService = FireStoreService(firestore)
    }

    @Test
    fun getUsersData() = runTest {
        val result = fireStoreService.getUser("테스트_계정").first()
        Log.d("result", result.toString())
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun setUsersData() = runTest {
        val user = UserEntity(
            id = "테스트_계정",
            name = "김민준",
            nickname = "히히",
            gender = "male",
            age = 24,
            location = LocationEntity(0, 0, "강북구"),
            phoneNumber = "01013456987"
        )
        fireStoreService.setUser(user)
    }
}