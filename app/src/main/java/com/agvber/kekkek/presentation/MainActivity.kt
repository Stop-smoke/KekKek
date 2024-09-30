package com.agvber.kekkek.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.kakao.sdk.common.util.Utility
import com.agvber.kekkek.BuildConfig
import com.agvber.kekkek.R
import com.agvber.kekkek.core.domain.repository.UserRepository
import com.agvber.kekkek.databinding.ActivityMainBinding
import com.agvber.kekkek.presentation.utils.defaultNavigationOption
import com.agvber.kekkek.presentation.utils.newBuilder
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

        navController.currentBackStackEntryFlow.collectLatestWithLifecycle(lifecycle) { backStackEntry ->
            when (backStackEntry.destination.route) {
                "home_screen" -> {
                    selectNavItem(navItems[0].first)
                }

                "community_screen" -> {
                    selectNavItem(navItems[1].first)
                }

                "my_screen" -> {
                    selectNavItem(navItems[2].first)
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
            }
        }
    }

    private fun navigateWithAnimation(@IdRes idRes: Int) {
        val navigationOptions = defaultNavigationOption.newBuilder()
            .setPopUpTo(destinationId = idRes, inclusive = true, saveState = true)
            .setLaunchSingleTop(true)
            .build()

        navController.navigate(
            resId = idRes,
            args = null,
            navOptions = navigationOptions
        )
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