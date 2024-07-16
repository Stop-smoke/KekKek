package com.stopsmoke.kekkek.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.common.util.Utility
import com.stopsmoke.kekkek.BuildConfig
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.databinding.ActivityMainBinding
import com.stopsmoke.kekkek.presentation.utils.defaultNavigationOption
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupBottomNavigation()
        showHashKey()
    }

    private fun showHashKey() {
        if (BuildConfig.DEBUG) {
            Log.d("MainActivity", "keyhash : ${Utility.getKeyHash(this)}")
        }
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

        navItems.forEach { (itemLayout, _) ->
            itemLayout.setOnClickListener {
                val currentDestinationId = navController.currentDestination?.id

                when (itemLayout.id) {
                    R.id.nav_home -> {
                        if (currentDestinationId != R.id.home) {
                            navigateWithAnimation(R.id.home)
                        }
                    }

                    R.id.nav_community -> {
                        if (currentDestinationId != R.id.community) {
                            navigateWithAnimation(R.id.community)
                        }
                    }

                    R.id.nav_mypage -> {
                        if (currentDestinationId != R.id.my_page) {
                            navigateWithAnimation(R.id.my_page)
                        }
                    }
                }
                selectNavItem(itemLayout)
            }
        }
        selectNavItem(binding.navHome)
    }

    private fun navigateWithAnimation(@IdRes idRes: Int) {
        navController.navigate(resId = idRes, args = null, navOptions = defaultNavigationOption)
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