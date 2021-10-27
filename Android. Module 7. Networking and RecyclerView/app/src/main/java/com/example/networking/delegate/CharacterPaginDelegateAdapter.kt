package com.example.networking.delegate

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class CharacterPaginDelegateAdapter<T : Any>(
    adapterDelegateManager: AdapterDelegateManager<T>,
    diffUtil: DiffUtil.ItemCallback<T>
): PagingDelegationAdapter<T>(adapterDelegateManager, diffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}