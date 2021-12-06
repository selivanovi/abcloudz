package com.example.networking.ui.recyclerviews.episodes

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EpisodeLayoutManager: RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        Log.d(EpisodeLayoutManager.toString(), "onLayoutChildren")
        if (state.itemCount <= 0) return
        fill(recycler, state.itemCount)
    }

    private fun fill(recycler: RecyclerView.Recycler, itemCount: Int) {

        var viewTop = 0
        val viewHeight = (height * VIEW_WIDTH_PERCENT).toInt()
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithDecorationsAndMargins(view, widthSpec, heightSpec)
            val decoratedMeasureWidth = getDecoratedMeasuredWidth(view)
            layoutDecorated(view, 0, viewTop, decoratedMeasureWidth, viewHeight + viewTop)
            viewTop = getDecoratedBottom(view)
        }

    }

    private fun measureChildWithDecorationsAndMargins(
        child: View,
        widthSpec: Int,
        heightSpec: Int
    ) {
        val decorRect = Rect()
        calculateItemDecorationsForChild(child, decorRect)
        val lp = child.layoutParams as RecyclerView.LayoutParams

        val height = updateSpaceWithExtra(
            heightSpec,
            lp.topMargin + decorRect.top,
            lp.bottomMargin + decorRect.bottom
        )
        Log.d(EpisodeLayoutManager.toString(), "Bottom margin ${lp.bottomMargin}")
        val width = updateSpaceWithExtra(
            widthSpec,
            lp.leftMargin + decorRect.left,
            lp.rightMargin + decorRect.right
        )
        child.measure(width, height)
    }

    private fun updateSpaceWithExtra(spec: Int, startInset: Int, endInset: Int): Int {
        if (startInset == 0 && endInset == 0) {
            return spec
        }
        val mode = View.MeasureSpec.getMode(spec)
        if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            return View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(spec) - startInset - endInset, mode
            )
        }
        return spec
    }

    override fun canScrollVertically(): Boolean = true

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val delta = scrollVerticallyIntertnal(dy)
        Log.d(EpisodeLayoutManager.toString(), "ChildCount : ${childCount}")
        offsetChildrenVertical(-delta)
        return delta
    }

    private fun scrollVerticallyIntertnal(dy: Int): Int =
        when {
            childCount == 0 -> 0
            // Moving content down
            dy < 0 -> {
                val lastChild = getChildAt(0)!!
                if (getDecoratedTop(lastChild) >= 0) {
                    0
                } else {
                    dy
                }
            }
            // Moving content up
            dy > 0 -> {
                val lastChild = getChildAt(itemCount - 1)!!
                if (getDecoratedBottom(lastChild) <= height) {
                    0
                } else {
                    dy
                }
            }
            else -> 0
        }

    companion object {
        const val VIEW_WIDTH_PERCENT: Float = 0.5F
    }
}