package com.stopsmoke.kekkek.presentation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import com.kakao.sdk.common.util.Utility
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ActivityMainBinding
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
//            insets
//        }
        setupNavigation()
        setupBottomNavigation()
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")
    // 해시값 찾을 때 사용하세요
    // Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")


    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.fragmentContainerViewMain.id) as NavHostFragment
        navController = navHostFragment.navController

        val isOnboardingComplete = runBlocking {
            userRepository.isOnboardingComplete().first()
        }
        setNavGraph(isOnboardingComplete)
    }

    private fun setupBottomNavigation() = with(binding.bottomNavigationViewHome) {
        setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    navController.popBackStack(route = "home", inclusive = false)
                }

                R.id.community -> {
                    navController.popBackStack(route = "community", inclusive = false)
                }

                R.id.my_page -> {
                    navController.popBackStack(route = "my", inclusive = false)
                }
            }
            item.onNavDestinationSelected(navController)
        }

        itemIconTintList =
            ContextCompat.getColorStateList(this@MainActivity, R.color.bottom_nav_color)
        itemTextColor =
            ContextCompat.getColorStateList(this@MainActivity, R.color.bottom_nav_color)
    }

    private fun setNavGraph(isAlreadyLogin: Boolean) {
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if (isAlreadyLogin) {
            navGraph.setStartDestination(R.id.home)
        } else {
            navGraph.setStartDestination(R.id.authentication)
        }
        navController.setGraph(navGraph, null)
    }
}