package com.stopsmoke.kekkek

import android.text.Html
import android.text.Spannable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.stopsmoke.kekkek.domain.model.DateTimeUnit
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime
import com.stopsmoke.kekkek.presentation.MainActivity

fun FragmentActivity.visible() {
    (this as? MainActivity)?.let {
        val layout = findViewById<ConstraintLayout>(R.id.bottom_navigation)
        layout.visibility = View.VISIBLE
    }
}

fun FragmentActivity.invisible() {
    (this as? MainActivity)?.let {
        val layout = findViewById<ConstraintLayout>(R.id.bottom_navigation)
        layout.visibility = View.GONE
    }
}

fun FragmentActivity.isVisible(): Boolean {
    val layout = findViewById<ConstraintLayout>(R.id.bottom_navigation)
    return View.VISIBLE == layout.visibility
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

fun convertSpannableToHtml(spannable: Spannable): String {
    return Html.toHtml(spannable, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE)
}

fun convertHtmlToSpannable(html: String): Spannable {
    return Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT) as Spannable
}

fun<T> List<T>.addOrRemove(value: T): List<T> {
    if (contains(value)) {
        return this.toMutableList().apply {
            remove(value)
        }
    }
    return this.toMutableList().apply {
        add(value)
    }
}