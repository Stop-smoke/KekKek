package com.stopsmoke.kekkek.presentation

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.common.util.Utility
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ActivityMainBinding
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.home.center.HomeCenterFragment
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


    private fun setupBottomNavigation() {

        val navItems = listOf(
            binding.navHome to Pair(binding.ivNavHome, binding.tvNavHome),
            binding.navCommunity to Pair(binding.ivNavCommunity, binding.tvNavCommunity),
            binding.navMypage to Pair(binding.ivNavMypage, binding.tvNavMypage)
        )

        val unselectedColor = ContextCompat.getColor(this, R.color.gray_lightgray2)
        val selectedColor = ContextCompat.getColor(this, R.color.black)

        fun selectNavItem(selectedItem: View) {
            navItems.forEach { (itemLayout, views) ->
                val (imageView, textView) = views
                if (itemLayout == selectedItem) {
                    imageView.setColorFilter(selectedColor)
                    textView.setTextColor(selectedColor)
                } else {
                    imageView.setColorFilter(unselectedColor)
                    textView.setTextColor(unselectedColor)
                }
            }
        }

        var recentPosition = 0

        navItems.forEach { (itemLayout, _) ->
            itemLayout.setOnClickListener {
                val currentDestinationId = navController.currentDestination?.id
                val navOptions = NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_from_right_fade_in)
                    .setExitAnim(R.anim.fade_out)
                    .setPopEnterAnim(R.anim.slide_in_from_left_fade_in)
                    .setPopExitAnim(R.anim.fade_out)
                    .build()

                when (itemLayout.id) {
                    R.id.nav_home -> {
                        if (currentDestinationId != R.id.home) {
                            navController.navigate(R.id.home, null, navOptions)
                        }
                        recentPosition = 0
                    }
                    R.id.nav_community -> {
                        if (currentDestinationId != R.id.community) {
                            navController.navigate(R.id.community, null, navOptions)
                        }
                        recentPosition = 1
                    }
                    R.id.nav_mypage -> {
                        if (currentDestinationId != R.id.my_page) {
                            navController.navigate(R.id.my_page, null, navOptions)
                        }
                        recentPosition = 2
                    }
                }
                selectNavItem(itemLayout)
            }
        }
        selectNavItem(binding.navHome)
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