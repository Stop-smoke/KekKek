package com.stopsmoke.kekkek.firestorage.dao

import android.os.Build
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.stopsmoke.kekkek.firestorage.data.StorageDaoImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.InputStream

@RunWith(AndroidJUnit4::class)
class StorageDaoInstrumentedTest {

    private lateinit var storageDao: StorageDao

    @Before
    fun init() {
        storageDao = StorageDaoImpl(Firebase.storage.reference)
    }

    @Test
    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.TIRAMISU)
    fun uploadProfileImage() = runTest {
//        val inputStream = InputStream.nullInputStream()
//        storageDao.uploadFile(inputStream, "users/test_user/profile_image.png").
//        collect {
//            Log.d("TestResult: ", it.toString())
//        }
    }

    @Test
    fun deleteProfileImage() = runTest {
        storageDao.deleteFile("/users/user-test12/profile_image").first()
    }
}