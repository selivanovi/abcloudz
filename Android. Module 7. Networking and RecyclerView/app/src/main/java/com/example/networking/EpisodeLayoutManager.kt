package com.example.networking

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EpisodeLayoutManager() : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT
        )

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        if (state.itemCount <= 0) return
        fill(recycler, state.itemCount)
    }

    private fun fill(recycler: RecyclerView.Recycler, itemCount: Int) {

        var viewTop = 0
        var viewLeft = 0
        val viewHeight = (height * VIEW_WIDTH_PERCENT).toInt()
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        for (i in 0 until itemCount) {
            val view = recycler.getViewForPosition(i)
            addView(view)

            measureChildWithDecorationsAndMargins(view, widthSpec, heightSpec)
            val decoratedMeasureWidth = getDecoratedMeasuredWidth(view)
            layoutDecorated(view, viewLeft, viewTop, decoratedMeasureWidth, viewHeight + viewTop)
            viewTop = getDecoratedBottom(view)
            if (viewTop >= height) break
        }

//        val view = recycler.getViewForPosition(0)
//        addView(view)
//        val viewHeight = (height * VIEW_WIDTH_PERCENT).toInt()
//        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
//        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
//        measureChildWithDecorationsAndMargins(view, widthSpec, heightSpec)
//        layoutDecorated(view, 0, 0, width, viewHeight)
    }

    private fun fillFirstElement(view: View) {

    }

    private fun fillBottom(view: View) {
        addView(view)
        val viewHeight = (height * VIEW_WIDTH_PERCENT).toInt()
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        measureChildWithDecorationsAndMargins(view, widthSpec, heightSpec)
        layoutDecorated(view, 0, 0, width, viewHeight)
    }

    private fun fillTop(view: View) {
        addView(view)
        val viewHeight = (height * VIEW_WIDTH_PERCENT).toInt()
        val widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        measureChildWithDecorationsAndMargins(view, widthSpec, heightSpec)
        layoutDecorated(view, 0, 0, width, viewHeight)
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

    override fun canScrollVertically(): Boolean {
        return true
    }

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {

        return dy
    }

    companion object {
        const val VIEW_WIDTH_PERCENT: Float = 0.5F
    }
}