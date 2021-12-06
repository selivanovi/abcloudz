package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.characters.Origin

class UnknownCharacter(
    override var id: Int,
    var image: String?,
    var name: String?,
    var origin: Origin?,
    var species: String?,
    var status: String?,
) : DelegateAdapterItem {

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        if (item is UnknownCharacter) {
            item.image == this.image &&
                    item.origin == this.origin &&
                    item.species == this.species &&
                    item.status == this.status
        } else false
}