package com.example.networking.model

import com.example.networking.delegate.DelegateAdapterItem

class Episode(
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
): DelegateAdapterItem {
    override fun id(): Int = id

    override fun compareContent(item: DelegateAdapterItem): Boolean {
        return this == item
    }
}
