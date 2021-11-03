package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem

class Episode(
    val airData: String,
    val episode: String,
    val id: Int,
    val name: String,
): DelegateAdapterItem {
    override fun id(): Int = id

    override fun compareContent(item: DelegateAdapterItem): Boolean {
        return this == item
    }
}
