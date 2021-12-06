package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.characters.Origin

class DeadCharacter(
    override var id: Int,
    var name: String?,
    var origin: Origin?,
    var image: String?,
    var species: String?,
    var status: String?,
) : DelegateAdapterItem {

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        if (item is DeadCharacter) {
            item.image == this.image &&
                    item.origin == this.origin &&
                    item.species == this.species &&
                    item.status == this.status

        } else false
}