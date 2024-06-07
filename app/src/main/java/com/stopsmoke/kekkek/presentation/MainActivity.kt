package com.stopsmoke.kekkek.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            DatastoreHelper.isOnboardingComplete(this@MainActivity).collect { isComplete ->
                if (isComplete) {
                    navController.setGraph(R.navigation.nav_graph)
                } else {
                    navController.setGraph(R.navigation.nav_onboarding)
                }
            }
        }

        // 노가다처럼 보이지만, 네비게이션 상태 변화에 따라 동적 업데이트 때문에 -> 온보딩에서 네비게이션을 숨기기 위해선 해당 리스너가 필요
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onboarding_introduce,
                R.id.onboarding_name,
                R.id.onboarding_perday,
                R.id.onboarding_perpack,
                R.id.onboarding_price,
                R.id.onboarding_birth,
                R.id.onboarding_finish -> {
                    binding.bottomNavigationViewHome.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigationViewHome.visibility = View.VISIBLE
                }
            }
        }

        binding.bottomNavigationViewHome.setupWithNavController(navController)
        binding.bottomNavigationViewHome.itemIconTintList = ContextCompat.getColorStateList(this, R.color.bottom_nav_color)
        binding.bottomNavigationViewHome.itemTextColor = ContextCompat.getColorStateList(this, R.color.bottom_nav_color)
    }
}