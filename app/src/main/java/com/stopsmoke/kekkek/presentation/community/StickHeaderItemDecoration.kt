package com.stopsmoke.kekkek.presentation.community

import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// 애니메이션 효과를 주기 위해 ItemDecoration을 상속 받아 구현한다.
class StickHeaderItemDecoration(private val sectionCallback: SectionCallback) : RecyclerView.ItemDecoration() {

    /*
     * ItemDecoration에서는 adapter에 직접 접근하지 않아야 한다.
     * Interface를 생성하여 adapter에 필요한 정보를 가져온다.
     */
    interface SectionCallback {
        fun isHeader(position: Int): Boolean // 해당 position이 Header이고 고정 될 View인지 판단한다.
        fun getHeadLayoutView(list: RecyclerView, position: Int): View? // 해당 position에 해당 하는 뷰를 가져온다.
    }

    /*
     * RecyclerView 위에 새로운 뷰를 그린다.
     * onDrawOver 함수는 RecyclerView가 그려진 뒤에 호출 된다.
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val topChild = parent.getChildAt(0) ?: return // RecyclerView에 보이는 View의 0번째를 가져온다.
        val topChildPosition = parent.getChildAdapterPosition(topChild) // topChild에 해당하는 position을 가져온다.
        if (topChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        /*
         * Header
         * getHeaderLayoutView를 사용해 topChildPosition에 해당하는 뷰를 찾는다.
         */
        val currentHeader: View =
            sectionCallback.getHeadLayoutView(parent, topChildPosition) ?: return

        // View의 레이아웃 설정
        fixLayoutSize(parent, currentHeader, topChild.measuredHeight)

        val contactPoint = currentHeader.bottom // 현재 topChildPosition에 해당하는 뷰의 bottom을 구한다.


        val childInContact: View = getChildInContact(parent, contactPoint) ?: return

        // 인접한 뷰를 구하고 childInContact에 해당하는 position을 가져온다.
        val childAdapterPoint = parent.getChildAdapterPosition(childInContact)
        if (childAdapterPoint == -1) {
            return
        }

        /*
         * childAdapterPosition은 리스트뷰의 최 상단에 있을 때 moveHeader로 밀려나는 것 처럼 그리고,
         * 그 외에는 상단에 고정되어 있는 것처럼 보이도록 drawHeader로 그린다.
         */
        when {
            sectionCallback.isHeader(childAdapterPoint) -> moveHeader(
                c,
                currentHeader,
                childInContact
            )

            else -> drawHeader(c, currentHeader)
        }
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child.bottom > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }


    private fun moveHeader(c: Canvas, currentHeader: View, nextHeader: View) {
        c.save()
        c.translate(0f, nextHeader.top - currentHeader.height.toFloat())
        currentHeader.draw(c)
        c.restore()
    }

    private fun drawHeader(c: Canvas, header: View) {
        c.save()
        c.translate(0f, 0f)
        header.draw(c)
        c.restore()
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View, height: Int) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.EXACTLY
        )
        val childWidth: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            height
        )
        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}