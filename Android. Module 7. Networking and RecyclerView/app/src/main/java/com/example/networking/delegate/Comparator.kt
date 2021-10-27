package com.example.networking.delegate

import androidx.recyclerview.widget.DiffUtil

class Comparator : DiffUtil.ItemCallback<DelegateAdapterItem>() {

    override fun areItemsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean {
        return oldItem.id() == newItem.id()
    }

    override fun areContentsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean {
        return oldItem.compareContent(newItem)
    }
}