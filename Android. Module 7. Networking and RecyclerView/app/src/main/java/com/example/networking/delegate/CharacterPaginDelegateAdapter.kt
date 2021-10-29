package com.example.networking.delegate

import android.content.DialogInterface
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

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