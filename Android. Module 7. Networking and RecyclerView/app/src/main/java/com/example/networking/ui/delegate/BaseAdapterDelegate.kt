package com.example.networking.ui.delegate

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapterDelegate<T : Any>(
    @LayoutRes val layoutId: Int
) {

    abstract val map: Map<Class<*>, Int>

    abstract fun onCreateViewHolder(viewGroup: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T)
}