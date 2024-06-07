package com.stopsmoke.kekkek.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        setupNavigation()
        setupBottomNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.fragmentContainerViewMain.id) as NavHostFragment
        navController = navHostFragment.navController

        val isComplete = runBlocking {
            userRepository.isOnboardingComplete().first()
        }
        setNavGraph(isComplete)
    }

    private fun setupBottomNavigation() = with(binding.bottomNavigationViewHome) {
        setupWithNavController(navController)
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
            navGraph.setStartDestination(R.id.nav_onboarding)
        }
        navController.setGraph(navGraph, null)
    }
}