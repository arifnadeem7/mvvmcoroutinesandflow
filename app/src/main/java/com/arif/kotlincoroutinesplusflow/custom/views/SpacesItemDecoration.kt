package com.arif.kotlincoroutinesplusflow.custom.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpacesItemDecoration(private val topMargin: Int, private val startMargin: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.apply {
            left = startMargin
            right = startMargin
            bottom = topMargin
            top = if (parent.getChildLayoutPosition(view) == 0) {
                topMargin
            } else {
                0
            }
        }
    }
}