package com.example.networking.ui.delegate

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class PagingDelegationAdapter<T : Any>(
    protected val adapterDelegateManager: AdapterDelegateManager<T>,
    diffUtil: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        getItem(position)?.let {
            return adapterDelegateManager.getItemViewType(it)
        }
        throw error("Element not found ")
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { adapterDelegateManager.onBindViewHolder(holder, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        adapterDelegateManager.onCreateViewHolder(parent, viewType)

}