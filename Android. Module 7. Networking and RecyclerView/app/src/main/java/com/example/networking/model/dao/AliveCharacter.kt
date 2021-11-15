package com.example.networking.model.dao

import com.example.networking.ui.delegate.DelegateAdapterItem
import com.example.networking.model.network.characters.Origin

data class AliveCharacter(
    override var id: Int? = null,
    var image: String? = null,
    var name: String? = null,
    var origin: Origin? = null,
    var species: String? = null,
    var status: String? = null,
    val episode: List<String>? = null,
) : DelegateAdapterItem {

    override fun compareContent(item: DelegateAdapterItem): Boolean =
        if (item is AliveCharacter) {
            item.image == this.image &&
                    item.origin == this.origin &&
                    item.species == this.species &&
                    item.status == this.status &&
                    item.episode == item.episode

        } else false
}