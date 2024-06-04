package com.stopsmoke.kekkek.presentation.notification.recyclerview.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

class NotificationDividerItemDecoration(
    context: Context, orientation: Int
): DividerItemDecoration(context, orientation) {


    private val paint = Paint()

    init {
        paint.color = Color.parseColor("#F5F5F5")
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingStart
        val right = parent.width - parent.paddingEnd

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = (child.bottom + params.bottomMargin).toFloat()

            c.drawRect(left.toFloat(), top, right.toFloat(), top + 4, paint)
        }
    }
}