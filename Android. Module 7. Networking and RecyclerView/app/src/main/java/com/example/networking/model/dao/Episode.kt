package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem

class Episode(
    override val id: Int,
    val airData: String?,
    val episode: String?,
    val name: String?,
) : DelegateAdapterItem {

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        if (item is Episode) {
            item.airData == this.airData &&
                    item.episode == this.episode &&
                    item.name == this.name
        } else false
}