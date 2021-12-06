package com.example.networking.ui.recyclerviews.characters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.networking.ui.delegate.AdapterDelegateManager
import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.ui.delegate.PagingDelegationAdapter

class CharacterPaginDelegateAdapter(
    adapterDelegateManager: AdapterDelegateManager<DelegateAdapterItem>,
    diffUtil: DiffUtil.ItemCallback<DelegateAdapterItem>,
    var onClickListener: ((DelegateAdapterItem) -> Unit)? = null
) : PagingDelegationAdapter<DelegateAdapterItem>(adapterDelegateManager, diffUtil) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(position) ?: return

        onClickListener?.let { onClickListener ->
            holder.itemView.setOnClickListener {
                onClickListener.invoke(item)
            }
        }
    }
}