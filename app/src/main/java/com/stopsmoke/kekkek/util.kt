package com.stopsmoke.kekkek

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun FragmentActivity.visible() {
    val layout = findViewById<BottomNavigationView>(R.id.bottomNavigationView_home)
    layout.visibility = View.VISIBLE
}

fun FragmentActivity.invisible() {
    val layout = findViewById<BottomNavigationView>(R.id.bottomNavigationView_home)
    layout.visibility = View.GONE
}