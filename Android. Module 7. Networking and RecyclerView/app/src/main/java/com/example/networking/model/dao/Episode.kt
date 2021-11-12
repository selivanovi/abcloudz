package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem

class Episode(
    override val id: Int? = null,
    val airData: String? = null,
    val episode: String? = null,
    val name: String? = null,
) : DelegateAdapterItem {

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        if (item is Episode) {
            item.airData == this.airData &&
                    item.episode == this.episode &&
                    item.name == this.name
        } else false
}