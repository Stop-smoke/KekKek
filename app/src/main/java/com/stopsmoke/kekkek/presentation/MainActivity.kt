package com.stopsmoke.kekkek.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.presentation.community.CommunityFragment
import com.stopsmoke.kekkek.presentation.my.MyFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment(CommunityFragment())
    }

    private fun setFragment(fragment: Fragment){
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_test, fragment)
            .commit()
    }
}