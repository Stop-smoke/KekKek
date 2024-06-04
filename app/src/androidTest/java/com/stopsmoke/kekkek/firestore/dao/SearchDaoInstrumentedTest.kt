package com.stopsmoke.kekkek.firestore.dao

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.stopsmoke.kekkek.firestore.data.SearchDaoImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchDaoInstrumentedTest {

    private lateinit var searchDao: SearchDao

    @Before
    fun init() {
        searchDao = SearchDaoImpl(Firebase.firestore)
    }

    @Test
    fun getRecommendedKeyword() = runTest {
        searchDao.getRecommendedKeyword().first()
            .let {
                Log.d("RecommendedKeywordEntity", it.toString())
            }
    }
}