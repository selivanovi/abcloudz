package com.example.networking.ui.delegate


import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class AdapterDelegateManager<T : Any>(vararg delegates: BaseAdapterDelegate<T>) {

    private val _delegates = mutableSetOf<BaseAdapterDelegate<T>>()
    private val maps = mutableMapOf<Class<*>, Int>()


    init {
        delegates.forEach {
            _delegates.add(it)
            maps.putAll(it.map)
        }
    }

    fun getItemViewType(item: T): Int {
        Log.d("AdapterDelegateManager", "getItemViewType: ${item::javaClass.name}" )
        val i = maps[item::class.java]!!
        Log.d("AdapterDelegateManager", "getItemViewType: ${i}" )
        return i
    }

    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate: BaseAdapterDelegate<T> = searchKey(viewType)
        Log.d("AdapterDelegateManager", "onCreateViewHolder: ${delegate.toString()}" )
        return delegate.onCreateViewHolder(viewGroup)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        val delegate: BaseAdapterDelegate<T> =
            searchKey(holder.itemViewType)

        delegate.onBindViewHolder(holder, item)
    }

    private fun searchKey(viewType: Int): BaseAdapterDelegate<T> {
        val clazz = maps.keys.find {
            maps[it] == viewType
        }
        Log.d("AdapterDelegateManager", "searchKey: ${clazz?.name}" )
        val delegate = _delegates.find {
            it.map[clazz] == viewType
        }!!
        Log.d("AdapterDelegateManager", "searchKey: ${delegate.javaClass.name}" )
        return delegate
    }
}