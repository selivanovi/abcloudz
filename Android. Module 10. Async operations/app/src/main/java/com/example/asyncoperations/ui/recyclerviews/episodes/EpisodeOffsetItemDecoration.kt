package com.example.networking.ui.recyclerviews.episodes

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EpisodeOffsetItemDecoration(
    private val top: Int,
    private val right: Int,
    private val bottom: Int,
    private val left: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(top, right, bottom, left)
    }
}
