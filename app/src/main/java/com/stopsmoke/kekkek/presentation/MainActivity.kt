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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

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
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainerViewMain.id) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationViewHome.setupWithNavController(navController)

        // 클릭시 색상 변경용
        binding.bottomNavigationViewHome.itemIconTintList = ContextCompat.getColorStateList(this, R.color.bottom_nav_color)
        binding.bottomNavigationViewHome.itemTextColor = ContextCompat.getColorStateList(this, R.color.bottom_nav_color)
    }
}