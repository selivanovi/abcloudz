package com.example.networking.ui.delegate

import android.content.res.Resources
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView

class AdapterDelegateManager<T : Any>(vararg delegates: BaseAdapterDelegate<T>) {

    private val _delegates = SparseArrayCompat<BaseAdapterDelegate<T>>()

    init {
        delegates.forEach {
            addDelegate(it)
        }
    }

    private fun addDelegate(delegate: BaseAdapterDelegate<T>) {
        var viewType = _delegates.size()
        while(_delegates[viewType] != null) {
            viewType++
        }

        _delegates.put(viewType, delegate)
    }

    fun getItemViewType(item: T): Int {
        for (i in 0 until _delegates.size()) {
            val delegate = _delegates.valueAt(i)
            if (delegate.isForViewType(item)) {
                return _delegates.keyAt(i)
            }
        }
        throw Resources.NotFoundException()
    }

    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate: BaseAdapterDelegate<T> = _delegates.get(viewType) ?: error("Not found delegate")

        return delegate.onCreateViewHolder(viewGroup)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        val delegate: BaseAdapterDelegate<T> = _delegates.get(holder.itemViewType) ?: error("Not found delegate")

        delegate.onBindViewHolder(holder, item)
    }
}