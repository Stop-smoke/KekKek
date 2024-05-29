package com.stopsmoke.kekkek

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.clMain) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            view.updatePadding(0, insets.top,0, 0)
            WindowInsetsCompat.CONSUMED
        }

        setupBottomNavigation()
        if(savedInstanceState == null) {
            binding.bottomNavigationViewHome.selectedItemId = R.id.home
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationViewHome.setOnItemSelectedListener { menu ->
            when(menu.itemId) {
                R.id.home -> {
                    HomeFragment().changeFragment()
                }
                R.id.community -> {
//                    CommunityFragment().changeFragment()
                }
                R.id.my_page -> {
//                    MyPageFragment().changeFragment()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun Fragment.changeFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.fl_main,this).commit()
    }
}