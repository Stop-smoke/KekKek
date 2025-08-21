package com.agvber.kekkek.presentation.utils

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class CustomItemDecoration(private val color: Int, private val height: Int) : RecyclerView.ItemDecoration() {
    private val paint = Paint()

    init {
        paint.color = color
        paint.strokeWidth = height.toFloat()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + height

            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}