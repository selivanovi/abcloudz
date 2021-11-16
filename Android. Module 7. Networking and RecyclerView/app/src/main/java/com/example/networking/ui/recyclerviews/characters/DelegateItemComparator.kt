package com.example.networking.ui.recyclerviews.characters

import androidx.recyclerview.widget.DiffUtil
import com.example.networking.ui.delegate.DelegateAdapterItem

class DelegateItemComparator : DiffUtil.ItemCallback<DelegateAdapterItem>() {

    override fun areItemsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean {
        return oldItem.compareContent(newItem)
    }
}