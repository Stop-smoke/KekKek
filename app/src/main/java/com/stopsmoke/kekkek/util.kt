package com.stopsmoke.kekkek

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime

fun FragmentActivity.visible() {
    val layout = findViewById<ConstraintLayout>(R.id.bottom_navigation)
    layout.visibility = View.VISIBLE
}

fun FragmentActivity.invisible() {
    val layout = findViewById<ConstraintLayout>(R.id.bottom_navigation)
    layout.visibility = View.GONE
}

fun getRelativeTime(pastTime: ElapsedDateTime): String {
    val timeType = when (pastTime.elapsedDateTime) {
        DateTimeUnit.YEAR -> "년"
        DateTimeUnit.MONTH -> "달"
        DateTimeUnit.DAY -> "일"
        DateTimeUnit.WEEK -> "주"
        DateTimeUnit.HOUR -> "시간"
        DateTimeUnit.MINUTE -> "분"
        DateTimeUnit.SECOND -> "초"
    }
    return "${pastTime.number} ${timeType} 전"
}