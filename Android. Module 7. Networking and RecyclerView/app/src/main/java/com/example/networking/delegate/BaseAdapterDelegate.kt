package com.example.networking.delegate

import android.text.Layout
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterDelegate<T>(
    @LayoutRes val layoutId: Int
) {


    abstract fun isForViewType(item: T): Boolean

    abstract fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T)
}