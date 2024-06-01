package com.stopsmoke.kekkek.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.presentation.home.HomeFragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.ActivityMainBinding
import com.stopsmoke.kekkek.presentation.community.CommunityFragment
import com.stopsmoke.kekkek.presentation.my.MyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNavigation()

        if (savedInstanceState == null) {
            binding.bottomNavigationViewHome.selectedItemId = R.id.home
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationViewHome.setOnItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.home -> {
                    HomeFragment().changeFragment()
                }

                R.id.community -> {
                    CommunityFragment().changeFragment()
                }

                R.id.my_page -> {
                    MyFragment().changeFragment()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun Fragment.changeFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.main, this).commit()
    }
}